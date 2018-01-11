package rujianbin.oauth2.authorization.server.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;



/**
 * Created by rujianbin on 2017/12/29.
 * 因code授权后需要获取当前登录人信息，需具备以下条件
 * 1. 和应用共同使用session  故需要配置redis-session组件
 * 2. 自定义了授权页面，故需要配置 freemarker组件
 * 3. 当前登录人的接口也是资源。故配置ResourceServerConfigurerAdapter
 * 4. 在授权服务器对应的security上增加用于验证token的filter
 *
 */

@Configuration
@EnableResourceServer
public class RjbResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${oauth2.resource-id:xxxxxx}")
    private String resourceId;


    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService jdbcClientDetailsService;


    @Autowired
    private ResourceServerTokenServices tokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(resourceId);
        resources.tokenServices(tokenServices);
    }


    private static class OAuthRequestedMatcher implements RequestMatcher {
        public boolean matches(HttpServletRequest request) {
            String auth = request.getHeader("Authorization");
            // Determine if the client request contained an OAuth Authorization
            boolean haveOauth2Token = (auth != null) && auth.startsWith("Bearer");
            boolean haveAccessToken = request.getParameter("access_token") != null;
            return haveOauth2Token || haveAccessToken;
        }
    }
}
