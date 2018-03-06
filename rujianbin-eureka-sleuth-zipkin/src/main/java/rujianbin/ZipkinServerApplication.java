package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import rujianbin.common.utils.RjbStringUtils;
import rujianbin.common.utils.YamlPropertySourceFactory;
import zipkin.server.EnableZipkinServer;

import javax.security.auth.message.config.AuthConfigFactory;

/**
 * Created by rujianbin on 2018/3/6.
 */

@EnableZipkinServer
@SpringBootApplication
@PropertySource(value={"classpath:application-zipkin-server-config.yml"},factory=YamlPropertySourceFactory.class)
public class ZipkinServerApplication {

    private static final Logger log = LoggerFactory.getLogger(ZipkinServerApplication.class);

    public static void main(String[] args) {
        //解决tomcat 版本太高，org.apache.catalina.authenticator.AuthenticatorBase.getJaspicProvider报错问题
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        Environment env = new SpringApplicationBuilder().sources(
                ZipkinServerApplication.class).web(true).run(args).getEnvironment();
        log.info(RjbStringUtils.startupLog(env));
    }
}
