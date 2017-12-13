package rujianbin.autoconfiguration.datasource;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import rujianbin.common.utils.YamlPropertySourceFactory;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@PropertySource(value="classpath:application-datasource-config.yml",factory=YamlPropertySourceFactory.class)
public class DataSourceApplication {

}
