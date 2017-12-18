package rujianbin.autoconfiguration.mybatis;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import rujianbin.common.utils.YamlPropertySourceFactory;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@PropertySource(value="classpath:application-mybatis-config.yml",factory=YamlPropertySourceFactory.class)
public class MybatisApplication
{
    public static void main( String[] args )
    {
        System.out.println( "Hello mybatis!" );
    }
}
