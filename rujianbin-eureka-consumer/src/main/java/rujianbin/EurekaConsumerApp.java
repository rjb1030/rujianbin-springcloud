package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import rujianbin.common.utils.RjbStringUtils;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;

/**
 * Hello world!
 * http://127.0.0.1:7046/hello-consumer
 *
 * 使用配置中心（优先于本地配置）：
 * 确定配置中心服务以启动
 * pom引入spring-cloud-starter-config
 * 并且配置bootstrap.properties, 内容为http://127.0.0.1:7056/master/rujianbin-eureka-provider-dev.yml的拆解
 *  spring.application.name=rujianbin-eureka-consumer     #$(aplication) 应用名也是文件名
    spring.cloud.config.uri=http://127.0.0.1:7056         #配置中心地址或配置注册中心地址
    spring.cloud.config.profile=dev                       #配置文件profile
    spring.cloud.config.label=master                      #配置文件在git上的分支
 *
 *
 * 配置中心热更新变量
 * @RefreshScope 注解需要被热更新变量的java类
 * actuator端点/refresh 用post请求下，配置变量即会生效，但只更新对应实例。
 *
 * 通过消息总线更新服务的所有实例：http://127.0.0.1:7046/bus/refresh
 * 引入spring-cloud-starter-bus-amqp.jar
 * 加载application-rabbitmq-amqp-config.yml 由于单独配置了classpath:xxxx而配置中心那个中心无该配置文件，故会从本地加载。
 * 配置指向rabbitmq服务即可实现消息总线更新配置
 * 继续优化方案是：spring-cloud-starter-bus-amqp.jar也同步整合到configserver。bus/refresh发送到configserver并通过destination指定需要更新的实例
 *
 */
@EnableCircuitBreaker        //允许熔断断路
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
@PropertySource(value={"classpath:application-eureka-consumer-config.yml",
        "classpath:application-hystrix-config.yml",
        "classpath:application-ribbon-config.yml",
        "classpath:application-rabbitmq-amqp-config.yml",
        "classpath:application-sleuth-zipkin.yml"},factory=YamlPropertySourceFactory.class)
public class EurekaConsumerApp
{

    private static final Logger log = LoggerFactory.getLogger(EurekaConsumerApp.class);

    /**
     * 针对非feign模式请求 bibbon负载需要使用@LoadBalanced注解，对restTemplate请求拦截
     * @return
     */
    @Bean
    @LoadBalanced
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main( String[] args )
    {
        //解决tomcat 版本太高，org.apache.catalina.authenticator.AuthenticatorBase.getJaspicProvider报错问题
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        Environment env  = new SpringApplicationBuilder().sources(
                EurekaConsumerApp.class
        ,ActuatorApplication.class).web(true).run(args).getEnvironment();
        log.info(RjbStringUtils.startupLog(env));
        log.info("\n\trujianbin.testName:{}",env.getProperty("rujianbin.testName"));
    }
}
