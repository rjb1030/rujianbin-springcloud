package rujianbin;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import rujianbin.common.utils.YamlPropertySourceFactory;

/**
 * Hello world!
 *
 */

@ImportResource({"classpath:dubbo-config.xml"})
@SpringBootApplication(scanBasePackages={"rujianbin"},exclude={DataSourceAutoConfiguration.class})    //扫描dubbo组件的路径，配置在启动类上@SpringBootApplication属性scanBasePackages上
@PropertySource(value = "classpath:application-dubbo-config.yml",factory = YamlPropertySourceFactory.class)
public class DubboApplication
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
