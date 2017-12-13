package rujianbin.autoconfiguration.redis.session.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;


/**
 * 使用redis实现session共享：
 * 1. 引入pom对应的jar包 spring-boot-starter-redis  spring-session-data-redis
 * 2. 配置redis参数
 *     redis:
         cluster:
             max-redirects: 10
             nodes: 192.168.91.228:7000,192.168.91.228:7001,192.168.91.228:7002,192.168.91.228:7003,192.168.91.228:7004,192.168.91.228:7005
         timeout: 20000
         pool:
             max-idle: 20
             min-idle: 10
             max-active: 1000
             max-wait: -1
 * 3. @EnableRedisHttpSession 启用 redis session
 *       maxInactiveIntervalInSeconds 会使server.session.timeout失效
 * CookieSerializer 自定义使cookie路径到根目录，可以跨context共享。否则path是在自己的context下
 *
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1200,redisNamespace="rjb-session",redisFlushMode=RedisFlushMode.ON_SAVE)
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        //cookie名字
        defaultCookieSerializer.setCookieName("rjbSessionId");
        //存储路径
        defaultCookieSerializer.setCookiePath("/");
        return defaultCookieSerializer;
    }



}
