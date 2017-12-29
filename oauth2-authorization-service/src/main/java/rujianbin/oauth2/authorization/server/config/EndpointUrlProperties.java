package rujianbin.oauth2.authorization.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by rujianbin on 2017/12/29.
 */
@ConfigurationProperties(prefix = "oauth2.endpoint")
public class EndpointUrlProperties {

    private String tokenUrl;
    private String authorizeUrl;
    private String confirmAccessUrl;
    private String errorUrl;
    private String checkTokenUrl;
    private String tokenKeyUrl;

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public void setAuthorizeUrl(String authorizeUrl) {
        this.authorizeUrl = authorizeUrl;
    }

    public String getConfirmAccessUrl() {
        return confirmAccessUrl;
    }

    public void setConfirmAccessUrl(String confirmAccessUrl) {
        this.confirmAccessUrl = confirmAccessUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public String getCheckTokenUrl() {
        return checkTokenUrl;
    }

    public void setCheckTokenUrl(String checkTokenUrl) {
        this.checkTokenUrl = checkTokenUrl;
    }

    public String getTokenKeyUrl() {
        return tokenKeyUrl;
    }

    public void setTokenKeyUrl(String tokenKeyUrl) {
        this.tokenKeyUrl = tokenKeyUrl;
    }
}
