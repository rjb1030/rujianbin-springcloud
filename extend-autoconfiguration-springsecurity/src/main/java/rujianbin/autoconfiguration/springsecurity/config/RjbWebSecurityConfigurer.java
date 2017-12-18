package rujianbin.autoconfiguration.springsecurity.config;

import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import rujianbin.autoconfiguration.springsecurity.config.handle.authorizematch.AuthorizeMatchProperties;
import rujianbin.autoconfiguration.springsecurity.config.handle.login.*;
import rujianbin.autoconfiguration.springsecurity.config.handle.logout.LogoutHandleProperties;
import rujianbin.autoconfiguration.springsecurity.config.handle.logout.RjbLogoutFilter;
import rujianbin.autoconfiguration.springsecurity.config.handle.logout.RjbLogoutSuccessHandle;

import javax.annotation.Resource;
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


//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/webjars/**", "/images/**");
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{
        //是否允许iframe嵌入 DENY,SAMEORIGIN,ALLOW-FROM;   此处允许同域名的iframe嵌入
        http.headers().frameOptions().sameOrigin();

        //拒绝请求
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage(accessDeniedUrl);
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        //permitAll的请求配置

        http.authorizeRequests().antMatchers(authorizeMatchProperties.getStaticResources().split(",")).permitAll()
                .antMatchers(authorizeMatchProperties.getPermitAllUrl().split(",")).permitAll();
        //权限的请求配置
        if(CollectionUtils.isEmpty(authorizeMatchProperties.getAuthorityUrl())){
            for(Map.Entry<String,Object> url : authorizeMatchProperties.getAuthorityUrl().entrySet()){
                http.authorizeRequests().antMatchers(url.getKey()).hasAuthority(url.getValue().toString());
            }
        }
        http.authorizeRequests().anyRequest().authenticated();
        //login配置
        RjbLoginSuccessHandler loginSuccessHandle = new RjbLoginSuccessHandler(loginHandleProperties.getSuccessUrl());
        RjbLoginFailureHandler loginFailureHandler = new RjbLoginFailureHandler(loginHandleProperties.getFailureUrl());
        RjbLoginFilter loginFilter = new RjbLoginFilter(loginHandleProperties.getLoginProcessingUrl(),loginSuccessHandle,loginFailureHandler,this.authenticationManager(),new RjbAuthenticationDetailsSource());
        http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);
        //logout配置
        RjbLogoutSuccessHandle rjbLogoutSuccessHandle = new RjbLogoutSuccessHandle(logoutHandleProperties.getLogoutSuccessUrl());
        RjbLogoutFilter logoutFilter = new RjbLogoutFilter(logoutHandleProperties.getLogoutUrl(),rjbLogoutSuccessHandle,new SecurityContextLogoutHandler(),new CookieClearingLogoutHandler(logoutHandleProperties.getSessionCookieName()));
        http.addFilterAt(logoutFilter, LogoutFilter.class);

        http.csrf().disable();
        http.sessionManagement().sessionAuthenticationErrorUrl(loginHandleProperties.getLoginPage()+"?session-auth-error").invalidSessionUrl(loginHandleProperties.getLoginPage()+"?invalid-session")
                /*  org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy
                    里面的checkAuthenticationAllowed方法，调用了sessionRegistry.getAllSessions,用authentication.getPrincipal()，也就是UserDetails实现对象。
                    但是sessionRegistry存储的时候使用的是Hash的数据结构，所以UserDetails实现类必须重写equals和hashCode
                    max-sessions只容许一个账号登录，error-if-maximum-exceeded=false 后面账号登录后踢出前一个账号，expired-url session过期跳转界面
                */
                .maximumSessions(1).maxSessionsPreventsLogin(true).expiredUrl(loginHandleProperties.getLoginPage()+"?session-timeout");

    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //自定义用户和权限查询
        RjbAuthenticationProvider provider = new RjbAuthenticationProvider(false);
        provider.setUserDetailsService(rjbUserDetailsService);
        provider.setPasswordEncoder(new CustomPasswordEncoder());
        auth.authenticationProvider(provider);

    }



}
