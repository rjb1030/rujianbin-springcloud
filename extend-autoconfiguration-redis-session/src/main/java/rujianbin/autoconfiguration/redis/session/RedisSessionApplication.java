package rujianbin.autoconfiguration.redis.session;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import rujianbin.common.utils.YamlPropertySourceFactory;


/**
 * Hello world!
 *
 */
@SpringBootApplication
@PropertySource(value = "classpath:application-redis-single-session-config.yml",factory = YamlPropertySourceFactory.class)    //该注解默认加载properties文件，如果要yml文件 则属性factory = YamlPropertySourceFactory.class
public class RedisSessionApplication {
    public static void main( String[] args ){
        System.out.println( "Hello World! RedisSessionApplication !!" );
    }
}
