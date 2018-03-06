package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import rujianbin.common.utils.RjbStringUtils;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;

/**
 * Hello world!
 *
 * 服务监控页面  http://127.0.0.1:7040/
 */
@EnableEurekaServer
@SpringBootApplication
@PropertySource(value="classpath:application-eureka-server-config.yml",factory=YamlPropertySourceFactory.class)
public class EurekaServerApp {

    private static final Logger log = LoggerFactory.getLogger(EurekaServerApp.class);

    public static void main( String[] args ) {

        //解决tomcat 版本太高，org.apache.catalina.authenticator.AuthenticatorBase.getJaspicProvider报错问题
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        Environment env  =  new SpringApplicationBuilder().sources(
                EurekaServerApp.class).web(true).run(args).getEnvironment();
        log.info(RjbStringUtils.startupLog(env));
    }
}
