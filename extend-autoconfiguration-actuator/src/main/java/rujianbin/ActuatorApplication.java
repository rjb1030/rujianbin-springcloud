package rujianbin;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import rujianbin.common.utils.YamlPropertySourceFactory;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@PropertySource(value="classpath:application-actuator-config.yml",factory=YamlPropertySourceFactory.class)
public class ActuatorApplication
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
