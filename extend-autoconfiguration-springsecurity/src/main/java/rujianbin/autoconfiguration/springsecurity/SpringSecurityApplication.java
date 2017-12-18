package rujianbin.autoconfiguration.springsecurity;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import rujianbin.common.utils.YamlPropertySourceFactory;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@PropertySource(value = "classpath:application-springsecurity-config.yml",factory = YamlPropertySourceFactory.class)
public class SpringSecurityApplication
{
    public static void main( String[] args )
    {
        System.out.println( "Hello spring-security!" );
    }
}
