package rujianbin;

import feign.Logger;
import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
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
 * actuator端点/refresh 用post请求下，配置变量即会生效
 *
 */
@EnableCircuitBreaker
@EnableFeignClients
@EnableDiscoveryClient      //@EnableFeignClients和@EnableDiscoveryClient 貌似有冲突，导致启动不了
@SpringBootApplication
@PropertySource(value={"classpath:application-eureka-consumer-config.yml","classpath:application-hystrix-config.yml","classpath:application-ribbon-config.yml"},factory=YamlPropertySourceFactory.class)
public class EurekaConsumerApp
{

    /**
     * feign请求的日志输出，同时需要配置文件制定@FeignClient制定的客户端类为DEBUG
     * 默认是Logger.Level.NONE 不记录任何日志，故需要调整级别
     * NONE: 不记录日志
     * BASIC：仅记录请求方法，URL以及响应状态码和执行时间
     * HEADERS: 记录除了BASIC的信息之外还会加上HEADER
     * FULL 全部信息
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

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
        ApplicationContext ctx = new SpringApplicationBuilder().sources(
                EurekaConsumerApp.class
        ,ActuatorApplication.class).web(true).run(args);
    }
}
