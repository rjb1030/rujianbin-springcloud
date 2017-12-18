package rujianbin.autoconfiguration.springsecurity.config.handle.login;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by rujianbin on 2017/12/14.
 */
@ConfigurationProperties(prefix = "springsecurity.login-handle")
public class LoginHandleProperties {

    private String loginPage;
    private String loginProcessingUrl;
    private String failureUrl;
    private String successUrl;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public String getLoginProcessingUrl() {
        return loginProcessingUrl;
    }

    public void setLoginProcessingUrl(String loginProcessingUrl) {
        this.loginProcessingUrl = loginProcessingUrl;
    }

    public String getFailureUrl() {
        return failureUrl;
    }

    public void setFailureUrl(String failureUrl) {
        this.failureUrl = failureUrl;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }
}
