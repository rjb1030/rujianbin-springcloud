package rujianbin;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import org.springframework.context.annotation.PropertySource;
import rujianbin.autoconfiguration.mybatis.MybatisApplication;
import rujianbin.auoconfiguration.freemarker.FreemarkApplication;
import rujianbin.autoconfiguration.datasource.DataSourceApplication;
import rujianbin.autoconfiguration.redis.session.RedisSessionApplication;
import rujianbin.common.utils.YamlPropertySourceFactory;

import javax.security.auth.message.config.AuthConfigFactory;



@SpringBootApplication
public class AppWebApplication {

    public static void main(String[] args) {

        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        ApplicationContext ctx = new SpringApplicationBuilder().sources(
                AppWebApplication.class
                ,DataSourceApplication.class
                ,RedisSessionApplication.class
                ,FreemarkApplication.class
                ,MybatisApplication.class
                ).web(true).run(args);

    }
}
