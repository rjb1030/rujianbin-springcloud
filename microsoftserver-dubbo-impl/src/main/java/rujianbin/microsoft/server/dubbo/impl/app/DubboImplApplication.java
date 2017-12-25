package rujianbin.microsoft.server.dubbo.impl.app;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import rujianbin.DubboApplication;

import javax.security.auth.message.config.AuthConfigFactory;
import javax.sql.DataSource;

/**
 * Created by rujianbin on 2017/12/25.
 * 由于springboot默认加载了DataSourceAutoConfiguration 注入了dataSource。 如果仅仅是demo测试，exclude dataSource即可
 */

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class DubboImplApplication {

    public static void main(String[] args) {

        if (AuthConfigFactory.getFactory() == null) {
            AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
        }
        ApplicationContext ctx = new SpringApplicationBuilder().sources(
                DubboImplApplication.class
                ,DubboApplication.class
        ).web(true).run(args);

    }

}
