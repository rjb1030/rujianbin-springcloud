package rujianbin.app.thirdparty.platform.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by rujianbin on 2018/1/2.
 */
@ConfigurationProperties(prefix = "oauth2.client.authorize")
public class OauthClientProperties {


    private String accessTokenUri;
    private String authorizeUrl;
    private String clientId;
    private String clientSecret;
    private String scope;
    private String redirectUri;

    public String getAccessTokenUri() {
        return accessTokenUri;
    }

    public void setAccessTokenUri(String accessTokenUri) {
        this.accessTokenUri = accessTokenUri;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }
}
