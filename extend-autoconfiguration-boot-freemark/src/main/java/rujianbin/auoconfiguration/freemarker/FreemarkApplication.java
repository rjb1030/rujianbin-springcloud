package rujianbin.auoconfiguration.freemarker;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import rujianbin.common.utils.YamlPropertySourceFactory;


@SpringBootApplication
@PropertySource(value="classpath:application-freemarker-config.yml",factory=YamlPropertySourceFactory.class)
public class FreemarkApplication {

}
