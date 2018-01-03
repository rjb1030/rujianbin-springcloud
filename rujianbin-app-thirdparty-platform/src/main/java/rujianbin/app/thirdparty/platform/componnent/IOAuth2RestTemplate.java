package rujianbin.app.thirdparty.platform.componnent;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;

/**
 * Created by rujianbin@.com on 2017/5/9.
 */
public interface IOAuth2RestTemplate {

    public OAuth2RestTemplate getTemplate(String code);
}
