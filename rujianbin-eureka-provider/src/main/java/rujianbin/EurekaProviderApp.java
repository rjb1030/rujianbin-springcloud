package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import rujianbin.common.utils.RjbStringUtils;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;

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


    接口多版本控制（灰度的基础）
    1. 引入fm-cloud-starter-bamboo.jar
    2. 配置eureka.instance.metadata-map.versions=1,2  即支持接口添加?version=1或2访问


 */

@EnableDiscoveryClient
@SpringBootApplication
@PropertySource(value={"classpath:application-eureka-provider-config.yml",
        "classpath:application-sleuth-zipkin.yml"}, factory=YamlPropertySourceFactory.class)
public class EurekaProviderApp
{

    private static final Logger log = LoggerFactory.getLogger(EurekaProviderApp.class);

    public static void main( String[] args ) {
        //解决tomcat 版本太高，org.apache.catalina.authenticator.AuthenticatorBase.getJaspicProvider报错问题
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        Environment env  = new SpringApplicationBuilder().sources(
                EurekaProviderApp.class).web(true).run(args).getEnvironment();
        log.info(RjbStringUtils.startupLog(env));

    }
}
