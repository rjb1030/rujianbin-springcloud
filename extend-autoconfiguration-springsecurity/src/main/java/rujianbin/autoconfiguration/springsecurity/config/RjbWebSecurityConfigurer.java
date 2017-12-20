package rujianbin.autoconfiguration.springsecurity.config;

import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.session.ConcurrentSessionFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import rujianbin.autoconfiguration.springsecurity.config.handle.authorizematch.AuthorizeMatchProperties;
import rujianbin.autoconfiguration.springsecurity.config.handle.login.*;
import rujianbin.autoconfiguration.springsecurity.config.handle.logout.LogoutHandleProperties;
import rujianbin.autoconfiguration.springsecurity.config.handle.logout.RjbLogoutFilter;
import rujianbin.autoconfiguration.springsecurity.config.handle.logout.RjbLogoutSuccessHandle;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by rujianbin on 2017/12/14.
 */

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({MybatisProperties.class,AuthorizeMatchProperties.class,LoginHandleProperties.class,LogoutHandleProperties.class})
public class RjbWebSecurityConfigurer extends WebSecurityConfigurerAdapter {


    @Resource(name="rjbUserDetailsService")
    private RjbUserDetailsService rjbUserDetailsService;

    @Autowired
    private AuthorizeMatchProperties authorizeMatchProperties;
    @Autowired
    private LogoutHandleProperties logoutHandleProperties;
    @Autowired
    private LoginHandleProperties loginHandleProperties;
    @Value("${springsecurity.access-denied-handler.url}")
    private String accessDeniedUrl;


    @Override
    public void configure(WebSecurity web) throws Exception {
        //静态资源不拦截
        web.ignoring().antMatchers(authorizeMatchProperties.getStaticResources().split(","));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        /**
         * 是否允许iframe嵌入 DENY,SAMEORIGIN,ALLOW-FROM;   此处允许同域名的iframe嵌入
         */
        http.headers().frameOptions().sameOrigin();

        /**
         * 放行请求
         * permitAll的请求配置
         */
        http.authorizeRequests().antMatchers(authorizeMatchProperties.getPermitAllUrl().split(",")).permitAll();
        /**
         * 拦截请求
         */
        if(CollectionUtils.isEmpty(authorizeMatchProperties.getAuthorityUrl())){
            for(Map.Entry<String,Object> url : authorizeMatchProperties.getAuthorityUrl().entrySet()){
                http.authorizeRequests().antMatchers(url.getKey()).hasAuthority(url.getValue().toString());
            }
        }
        http.authorizeRequests().anyRequest().authenticated();

        /**
         * 拒绝策略（ExceptionTranslationFilter）
         * AccessDeniedHandlerImpl 制定权限不够时跳转页面
         * formLogin().loginPage 制定未授权（未登录）下跳转登录页
         */
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage(accessDeniedUrl);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
        http.formLogin().loginPage(loginHandleProperties.getLoginPage()+"?noAuth");


        /**
         * 登录策略（LoginFilter）
         * 如果要控制并发登录数 session策略需要自定义（SessionAuthenticationStrategy）
         */
        RjbLoginSuccessHandler loginSuccessHandle = new RjbLoginSuccessHandler(loginHandleProperties.getSuccessUrl());
        RjbLoginFailureHandler loginFailureHandler = new RjbLoginFailureHandler(loginHandleProperties.getFailureUrl());
        RjbLoginFilter loginFilter = new RjbLoginFilter(loginHandleProperties.getLoginProcessingUrl(),loginSuccessHandle,loginFailureHandler,this.authenticationManager(),new RjbAuthenticationDetailsSource());
        //session策略，只能登陆一个。控制session互踢
        SessionRegistry sessionRegistry = new SessionRegistryImpl();
        loginFilter.setSessionAuthenticationStrategy(compositeSessionAuthenticationStrategy(sessionRegistry));
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);


        /**
         * 登出策略（LogoutFilter）
         */
        RjbLogoutSuccessHandle rjbLogoutSuccessHandle = new RjbLogoutSuccessHandle(logoutHandleProperties.getLogoutSuccessUrl());
        RjbLogoutFilter logoutFilter = new RjbLogoutFilter(logoutHandleProperties.getLogoutUrl(),rjbLogoutSuccessHandle,new SecurityContextLogoutHandler(),new CookieClearingLogoutHandler(logoutHandleProperties.getSessionCookieName()));
        http.addFilterAt(logoutFilter, LogoutFilter.class);


        /**
         * csrf校验
         */
        http.csrf().disable();


        /**
         * session管理  loginFilter自定义后，需要手动添加ConcurrentSessionFilter 有2种方式 1. 手动addFilter   2.http.sessionManagement()构建
         * sessionManagement会自动构建ConcurrentSessionFilter和SessionManagementFilter 加入filter链
         * 注意：并发登录控制时判断用户是否同一个，authentication.getPrincipal()，也就是UserDetails实现对象，实现类必须重写equals和hashCode
         *
         *
         * http.addFilterAt(new ConcurrentSessionFilter(sessionRegistry,loginHandleProperties.getLoginPage()+"?session-expired"),ConcurrentSessionFilter.class);
         */
        http.sessionManagement().sessionAuthenticationErrorUrl(loginHandleProperties.getLoginPage()+"?session-auth-error").invalidSessionUrl(loginHandleProperties.getLoginPage()+"?invalid-session")
                .maximumSessions(1).maxSessionsPreventsLogin(true)   //此处2个配置无效，由loginFilter里的session策略配置
                .expiredUrl(loginHandleProperties.getLoginPage()+"?session-expired")
                .sessionRegistry(sessionRegistry);

    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //自定义用户和权限查询
        RjbAuthenticationProvider provider = new RjbAuthenticationProvider(false);
        provider.setUserDetailsService(rjbUserDetailsService);
        provider.setPasswordEncoder(new CustomPasswordEncoder());
        auth.authenticationProvider(provider);

    }



    /**
     * 登录的session策略，自定义loginFilter时需要同时自定义session策略
     * SessionRegistry sessionRegistry 如果是分布式session 则SessionRegistry需要重写。默认是内存对象里校验登录session数
     * @param sessionRegistry
     * @return
     */
    private CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy(SessionRegistry sessionRegistry){

        List<SessionAuthenticationStrategy> delegateStrategies = new ArrayList<>();

        ConcurrentSessionControlAuthenticationStrategy sessionAuthenticationStrategy = new ConcurrentSessionControlAuthenticationStrategy(sessionRegistry);
        sessionAuthenticationStrategy.setMaximumSessions(1);
        sessionAuthenticationStrategy.setExceptionIfMaximumExceeded(false);
        delegateStrategies.add(sessionAuthenticationStrategy);

        SessionFixationProtectionStrategy sessionFixationProtectionStrategy = new SessionFixationProtectionStrategy();
        delegateStrategies.add(sessionFixationProtectionStrategy);

        RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy = new RegisterSessionAuthenticationStrategy(sessionRegistry);
        delegateStrategies.add(registerSessionAuthenticationStrategy);

        CompositeSessionAuthenticationStrategy compositeSessionAuthenticationStrategy = new CompositeSessionAuthenticationStrategy(delegateStrategies);
        return compositeSessionAuthenticationStrategy;
    }


}
