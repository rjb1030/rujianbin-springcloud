package rujianbin.oauth2.authorization.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.DefaultUserApprovalHandler;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by rujianbin on 2017/12/26.
 */

@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(EndpointUrlProperties.class)
public class RjbAuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter{

    @Autowired
    private EndpointUrlProperties endpointUrlProperties;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService jdbcClientDetailsService;

    @Autowired
    private AuthorizationCodeServices authorizationCodeServices;

    @Autowired
    private AuthorizationServerTokenServices tokenServices;

    @Autowired
    private UserApprovalHandler oauthUserApprovalHandler;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.setClientDetailsService(jdbcClientDetailsService);
        endpoints.tokenStore(tokenStore)
                .tokenServices(tokenServices)
                .userApprovalHandler(oauthUserApprovalHandler)
                .authorizationCodeServices(authorizationCodeServices)
                .pathMapping("/oauth/authorize",endpointUrlProperties.getAuthorizeUrl())
                .pathMapping("/oauth/token",endpointUrlProperties.getTokenUrl())
                .pathMapping("/oauth/confirm_access",endpointUrlProperties.getConfirmAccessUrl())
                .pathMapping("/oauth/error",endpointUrlProperties.getErrorUrl())
                .pathMapping("/oauth/check_token",endpointUrlProperties.getCheckTokenUrl())
                .pathMapping("/oauth/token_key",endpointUrlProperties.getTokenKeyUrl())
                .authenticationManager(this.authenticationManager);
    }


    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.allowFormAuthenticationForClients();
        /**
         * 如果资源服务器使用RemoteTokenServices请求到授权服务器做token验证，则此处要放开权限 或指定权限。建议放开权限
         * 如果指定权限hasAuthority或authenticated()，则需要ClientCredentialsTokenEndpointFilter的URL也要拦截check-token,加载出权限信息
         */
        oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        // @formatter:off
//        clients.inMemory().withClient("tonr")
//                .resourceIds(SPARKLR_RESOURCE_ID)
//                .authorizedGrantTypes("authorization_code", "implicit")
//                .authorities("ROLE_CLIENT")
//                .scopes("read", "write")
//                .secret("secret")
//                .and()
//                .withClient("tonr-with-redirect")
//                .resourceIds(SPARKLR_RESOURCE_ID)
//                .authorizedGrantTypes("authorization_code", "implicit")
//                .authorities("ROLE_CLIENT")
//                .scopes("read", "write")
//                .secret("secret")
//                .redirectUris(tonrRedirectUri)
//                .and()
//                .withClient("my-client-with-registered-redirect")
//                .resourceIds(SPARKLR_RESOURCE_ID)
//                .authorizedGrantTypes("authorization_code", "client_credentials")
//                .authorities("ROLE_CLIENT")
//                .scopes("read", "trust")
//                .redirectUris("http://anywhere?key=value")
//                .and()
//                .withClient("my-trusted-client")
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
//                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
//                .scopes("read", "write", "trust")
//                .accessTokenValiditySeconds(60)
//                .and()
//                .withClient("my-trusted-client-with-secret")
//                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
//                .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
//                .scopes("read", "write", "trust")
//                .secret("somesecret")
//                .and()
//                .withClient("my-less-trusted-client")
//                .authorizedGrantTypes("authorization_code", "implicit")
//                .authorities("ROLE_CLIENT")
//                .scopes("read", "write", "trust")
//                .and()
//                .withClient("my-less-trusted-autoapprove-client")
//                .authorizedGrantTypes("implicit")
//                .authorities("ROLE_CLIENT")
//                .scopes("read", "write", "trust")
//                .autoApprove(true);

        clients.withClientDetails(jdbcClientDetailsService);
    }





    @Configuration
    static class RjbOauth2ServerConfig{

        @Value("${oauth2.token.access-token-validity-seconds}")
        private Integer accessTokenValiditySeconds;

        @Value("${oauth2.token.refresh-token-validity-seconds}")
        private Integer refreshTokenValiditySeconds;

        @Resource(name="dataSourceTwo")
        private DataSource dataSource;

        @Bean
        public TokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }

        @Bean
        public ClientDetailsService jdbcClientDetailsService(){
            return new JdbcClientDetailsService(dataSource);
        }

        @Bean
        public AuthorizationCodeServices authorizationCodeServices(){
            return new JdbcAuthorizationCodeServices(dataSource);
        }

        @Bean
        public UserDetailsService clientDetailsUserService(@Qualifier("jdbcClientDetailsService") ClientDetailsService clientDetailsService){
            return new ClientDetailsUserDetailsService(clientDetailsService);
        }

        /**
         * 实现了AuthorizationServerTokenServices  ResourceServerTokenServices 两个接口。授权端和资源端可以同时用这个tokenService
         * @param tokenStore
         * @param clientDetailsService
         * @return
         */
        @Bean
        public DefaultTokenServices tokenServices(@Qualifier("tokenStore") TokenStore tokenStore, @Qualifier("jdbcClientDetailsService") ClientDetailsService clientDetailsService){
            DefaultTokenServices tokenServices =  new DefaultTokenServices();
            tokenServices.setTokenStore(tokenStore);
            tokenServices.setClientDetailsService(clientDetailsService);
            tokenServices.setSupportRefreshToken(true);
            tokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
            tokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
            return tokenServices;
        }

        @Bean
        public UserApprovalHandler oauthUserApprovalHandler(){
            return new DefaultUserApprovalHandler();
        }

    }
}
