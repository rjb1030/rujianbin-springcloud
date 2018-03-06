package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import rujianbin.common.utils.RjbStringUtils;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;

/**
 * Created by rujianbin on 2018/2/12.
 */

@EnableZuulProxy
@SpringBootApplication
@PropertySource(value={"classpath:application-eureka-zuul-config.yml"},factory=YamlPropertySourceFactory.class)
public class EurekaZuulApplication {

    private static final Logger log = LoggerFactory.getLogger(EurekaZuulApplication.class);

    public static void main(String[] args) {
        //解决tomcat 版本太高，org.apache.catalina.authenticator.AuthenticatorBase.getJaspicProvider报错问题
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        Environment env = new SpringApplicationBuilder().sources(
                EurekaZuulApplication.class
                ,ActuatorApplication.class).web(true).run(args).getEnvironment();
        log.info(RjbStringUtils.startupLog(env));
    }
}
