package rujianbin.autoconfiguration.springsecurity.config.handle.logout;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by rujianbin on 2017/12/14.
 */
@ConfigurationProperties(prefix = "springsecurity.logout-handle")
public class LogoutHandleProperties {

    private String logoutUrl;
    private String logoutSuccessUrl;
    private String sessionCookieName;
    private Boolean invalidateHttpSession;

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getLogoutSuccessUrl() {
        return logoutSuccessUrl;
    }

    public void setLogoutSuccessUrl(String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
    }

    public String getSessionCookieName() {
        return sessionCookieName;
    }

    public void setSessionCookieName(String sessionCookieName) {
        this.sessionCookieName = sessionCookieName;
    }

    public Boolean getInvalidateHttpSession() {
        return invalidateHttpSession;
    }

    public void setInvalidateHttpSession(Boolean invalidateHttpSession) {
        this.invalidateHttpSession = invalidateHttpSession;
    }
}
