package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;

/**
 * Hello world!
 * 监控地址http://127.0.0.1:7050/hystrix
 * 监控单个实例： http://127.0.0.1:7046/hystrix.stream
 */
@EnableHystrixDashboard
@SpringBootApplication
@PropertySource(value={"classpath:application-hystrix-dashboard-config.yml"},factory=YamlPropertySourceFactory.class)
public class HystrixDashboardApplication
{


    public static void main( String[] args )
    {
        //解决tomcat 版本太高，org.apache.catalina.authenticator.AuthenticatorBase.getJaspicProvider报错问题
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        ApplicationContext ctx = new SpringApplicationBuilder().sources(
                HystrixDashboardApplication.class
        ,ActuatorApplication.class
                ).web(true).run(args);
    }
}
