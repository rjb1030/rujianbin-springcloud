package rujianbin.oauth2.authorization.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by rujianbin on 2017/12/27.
 */

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(EndpointUrlProperties.class)
public class RjbOauth2SecurityConfiguration extends WebSecurityConfigurerAdapter  {

    @Autowired
    private EndpointUrlProperties endpointUrlProperties;

    @Autowired
    private ClientDetailsService jdbcClientDetailsService;

    @Autowired
    private UserDetailsService clientDetailsUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private ResourceServerTokenServices resourceTokenServices;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(clientDetailsUserService);
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,endpointUrlProperties.getAuthorizeUrl()).authenticated()
                .antMatchers(HttpMethod.POST,endpointUrlProperties.getAuthorizeUrl()).authenticated()
                .antMatchers(HttpMethod.GET,endpointUrlProperties.getTokenUrl()).authenticated()
                .antMatchers(HttpMethod.POST,endpointUrlProperties.getTokenUrl()).authenticated()
                .antMatchers(endpointUrlProperties.getCheckTokenUrl()).authenticated()
                .anyRequest().authenticated();
        http.httpBasic().authenticationEntryPoint(authenticationEntryPoint);
        http.csrf().disable();
        http.addFilterBefore(clientCredentialsTokenEndpointFilter(), BasicAuthenticationFilter.class);


        /**
         * 资源服务器相关配置
         * 请求资源都是带着token的。如果在授权服务器上集成资源服务器。则需要添加token验证的filter
         */
        OAuth2AuthenticationProcessingFilter filter = new OAuth2AuthenticationProcessingFilter();
        filter.setStateless(false); //防止登录信息被清理
        OAuth2AuthenticationManager manager = new OAuth2AuthenticationManager();
        manager.setTokenServices(resourceTokenServices);
        filter.setAuthenticationManager(manager);
        http.addFilterBefore(filter,AbstractPreAuthenticatedProcessingFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public ClientCredentialsTokenEndpointFilter clientCredentialsTokenEndpointFilter(){
        ClientCredentialsTokenEndpointFilter filter = new ClientCredentialsTokenEndpointFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationEntryPoint(authenticationEntryPoint);
        filter.setAllowSessionCreation(false);
        filter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(endpointUrlProperties.getTokenUrl(),"POST"));
        return filter;
    }


    @Bean
    public AuthenticationEntryPoint oauth2AuthenticationEntryPoint(){
        return new OAuth2AuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler OAuth2AccessDeniedHandler(){
        return new OAuth2AccessDeniedHandler();
    }
}
