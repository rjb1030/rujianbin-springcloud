package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;

/**
 * Hello world!
 * @EnableDiscoveryClient 创建DiscoveryClient接口针对Eureka客户端的EurekaDiscoveryClient实例
 *
 * 使用配置中心（优先于本地配置）：
 * 确定配置中心服务以启动
 * pom引入spring-cloud-starter-config
 * 并且配置bootstrap.properties, 内容为http://127.0.0.1:7056/master/rujianbin-eureka-provider-dev.yml的拆解
 *  spring.application.name=rujianbin-eureka-provider     #$(aplication) 应用名也是文件名
    spring.cloud.config.uri=http://127.0.0.1:7056         #配置中心地址
    spring.cloud.config.profile=dev                       #配置文件profile
    spring.cloud.config.label=master                      #配置文件在git上的分支
 */

@EnableDiscoveryClient
@SpringBootApplication
@PropertySource(value="classpath:application-eureka-provider-config.yml",factory=YamlPropertySourceFactory.class)
public class EurekaProviderApp
{
    public static void main( String[] args )
    {
        //解决tomcat 版本太高，org.apache.catalina.authenticator.AuthenticatorBase.getJaspicProvider报错问题
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        ApplicationContext ctx = new SpringApplicationBuilder().sources(
                EurekaProviderApp.class).web(true).run(args);
    }
}
