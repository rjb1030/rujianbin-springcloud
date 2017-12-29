package rujianbin.oauth2.authorization.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenEndpointFilter;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by rujianbin on 2017/12/27.
 */

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(EndpointUrlProperties.class)
public class RjbOauth2SecurityConfiguration extends WebSecurityConfigurerAdapter implements Ordered {

    @Autowired
    private EndpointUrlProperties endpointUrlProperties;

    @Autowired
    private UserDetailsService clientDetailsUserService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(clientDetailsUserService);
    }

    @Override
    public int getOrder() {
        return 2;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET,endpointUrlProperties.getAuthorizeUrl()).authenticated()
                .antMatchers(HttpMethod.POST,endpointUrlProperties.getTokenUrl()).authenticated()
                .antMatchers(endpointUrlProperties.getCheckTokenUrl()).permitAll()
                .anyRequest().authenticated();
                http.httpBasic().authenticationEntryPoint(authenticationEntryPoint).and().csrf().disable();
        http.addFilterBefore(clientCredentialsTokenEndpointFilter(), BasicAuthenticationFilter.class);
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
