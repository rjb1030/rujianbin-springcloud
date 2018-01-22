package rujianbin.autoconfiguration.tomcat.customizer;

import org.apache.catalina.valves.AccessLogValve;
import org.apache.catalina.valves.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by rujianbin on 2018/1/19.
 */
@Component
public class RjbAccessLogValveCustomizer {

    @Autowired
    private TomcatServerProperties tomcatServerProperties;

    public  AccessLogValve getLogAccessLogValue() {
        AccessLogValve accessLogValve = new AccessLogValve();
        accessLogValve.setDirectory(tomcatServerProperties.getLog().getDirectory());
        accessLogValve.setEnabled(tomcatServerProperties.getLog().isEnabled());
        accessLogValve.setPattern(Constants.AccessLog.COMMON_PATTERN);
        accessLogValve.setPrefix(tomcatServerProperties.getLog().getPrefix());
        accessLogValve.setSuffix(tomcatServerProperties.getLog().getSuffix());
        return accessLogValve;
    }
}
