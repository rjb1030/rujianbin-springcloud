package rujianbin.autoconfiguration.springsecurity.config.handle.authorizematch;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rujianbin on 2017/12/14.
 */

@ConfigurationProperties(prefix = "springsecurity.authorize-match")
public class AuthorizeMatchProperties {

    private String staticResources;
    private String permitAllUrl;
    private Map<String,Object> authorityUrl = new HashMap<String,Object>();

    public String getStaticResources() {
        return staticResources;
    }

    public void setStaticResources(String staticResources) {
        this.staticResources = staticResources;
    }

    public String getPermitAllUrl() {
        return permitAllUrl;
    }

    public void setPermitAllUrl(String permitAllUrl) {
        this.permitAllUrl = permitAllUrl;
    }


    public Map<String, Object> getAuthorityUrl() {
        return authorityUrl;
    }

    public void setAuthorityUrl(Map<String, Object> authorityUrl) {
        this.authorityUrl = authorityUrl;
    }
}
