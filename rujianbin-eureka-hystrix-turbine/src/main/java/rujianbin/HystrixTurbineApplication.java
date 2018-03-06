package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import rujianbin.common.utils.RjbStringUtils;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;

/**
 * Created by rujianbin on 2018/2/9.
 *
 * 监控地址http://127.0.0.1:7051/hystrix
 * 输入http://127.0.0.1:7051/turbine.stream监控集群
 * 集群监控支持消息代理模式：hystrix客户端将监控信息输出到消息代理中，turbine服务再从消息代理中获取监控信息，并汇集展示到dashboard
 */

@EnableTurbine
@EnableDiscoveryClient
@SpringBootApplication
@PropertySource(value={"classpath:application-hystrix-turbine-dashboard-config.yml"},factory=YamlPropertySourceFactory.class)
public class HystrixTurbineApplication {

    private static final Logger log = LoggerFactory.getLogger(HystrixTurbineApplication.class);

    public static void main(String[] args) {
        //解决tomcat 版本太高，org.apache.catalina.authenticator.AuthenticatorBase.getJaspicProvider报错问题
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        Environment env  = new SpringApplicationBuilder().sources(
                HystrixTurbineApplication.class
                ,ActuatorApplication.class
        ).web(true).run(args).getEnvironment();
        log.info(RjbStringUtils.startupLog(env));
    }
}
