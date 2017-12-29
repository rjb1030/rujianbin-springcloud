package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@PropertySource(value = "classpath:application.yml",factory = YamlPropertySourceFactory.class)
@PropertySource(value = "classpath:application-oauth2-server-config.yml",factory = YamlPropertySourceFactory.class)
public class Oauth2ServerApplication
{
    public static void main( String[] args )
    {
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        ApplicationContext ctx = new SpringApplicationBuilder().sources(
                Oauth2ServerApplication.class

        ).web(true).run(args);
    }
}
