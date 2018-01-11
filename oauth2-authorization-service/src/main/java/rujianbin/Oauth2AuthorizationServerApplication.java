package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import rujianbin.auoconfiguration.freemarker.FreemarkApplication;
import rujianbin.autoconfiguration.datasource.DataSourceApplication;
import rujianbin.autoconfiguration.redis.session.RedisSessionApplication;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@PropertySource(value = "classpath:application.yml",factory = YamlPropertySourceFactory.class)
@PropertySource(value = "classpath:application-oauth2-server-config.yml",factory = YamlPropertySourceFactory.class)
public class Oauth2AuthorizationServerApplication
{
    public static void main( String[] args )
    {
        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        ApplicationContext ctx = new SpringApplicationBuilder().sources(
                Oauth2AuthorizationServerApplication.class
                ,DataSourceApplication.class
                ,RedisSessionApplication.class   //code授权，必须有登录信息。通过redis加载登录session
                ,FreemarkApplication.class   //用于返回自定义授权页面ftl 需要组件

        ).web(true).run(args);
    }
}
