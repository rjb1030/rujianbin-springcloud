package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import rujianbin.common.utils.RjbStringUtils;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;

/**
 * # 配置中心访问路径 http://127.0.0.1:7056/master/rujianbin-eureka-provider-dev.yml
 #/{aplication}-{profile}.yml
 #/{aplication}/{profile}[/{branch}]
 #/{branch}/{application}-{profile}.yml
 #/{application}-{profile}.properties
 #/{branch}/{application}-{profile}.properties



 # 本地文件系统,默认从configServer的resources目录下读取
 # spring.profiles.active=native      启用本地文件系统
 # spring.cloud.config.server.native.searchLocations   具体制定配置文件搜索位置，默认resoucrces


 # 配置加密
 # 1. 将local_policy.jar和US_export_policy.jar复制到$JAVA_HOME/jdk/jre/lib/security目录下，覆盖原来内容
 # 2. 配置encrypt.key  此时访问http://127.0.0.1:7056/encrypt/status 显示{"status":"OK"}
 # 3. 配置上带有{cipher}即会被解密处理
 # 4. 加解密URL  http://127.0.0.1:7056/encrypt  http://127.0.0.1:7056/decrypt  用post 参数data Content-Type=application/json
 *
 */
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
@PropertySource(value={"classpath:application-configcenter.yml"},factory=YamlPropertySourceFactory.class)
public class EurekaConfigCenterApplication
{
    private static final Logger log = LoggerFactory.getLogger(EurekaConfigCenterApplication.class);

    public static void main( String[] args )
    {
        //解决tomcat 版本太高，org.apache.catalina.authenticator.AuthenticatorBase.getJaspicProvider报错问题
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        Environment env = new SpringApplicationBuilder().sources(
                EurekaConfigCenterApplication.class
                ).web(true).run(args).getEnvironment();
        log.info(RjbStringUtils.startupLog(env));
    }
}
