package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;

/**
 * Hello world!
 *
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class,SecurityAutoConfiguration.class})
@PropertySource(value = "classpath:application-app-thirdparty-config.yml",factory = YamlPropertySourceFactory.class)
public class ThirdPartyApplication
{
    public static void main( String[] args )
    {
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        ApplicationContext ctx = new SpringApplicationBuilder().sources(
                ThirdPartyApplication.class

        ).web(true).run(args);
    }
}
