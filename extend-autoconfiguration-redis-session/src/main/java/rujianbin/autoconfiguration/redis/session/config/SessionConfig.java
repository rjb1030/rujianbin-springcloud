package rujianbin.autoconfiguration.redis.session.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;
import rujianbin.security.principal.author.RjbSecurityUser;

import java.text.SimpleDateFormat;


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
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600,redisNamespace="rjb-session",redisFlushMode=RedisFlushMode.ON_SAVE)
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

    @Bean
    public HttpSessionStrategy myHttpSessionStrategy(){
        return new MyHttpSessionStrategy();
    }

    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        /**
         * 如果只是security session用jackson序列化和反序列化，以下配置可用。如果再集成oauth2 则AuthorizationRequest的反序列化会报AuthorizationRequest is not whitelisted异常
         * 建议用jdk序列化
         */
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
//        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//        //解决security对象序列化时，会报没有默认构造函数或authentication属性无public方法
//        mapper.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));
//        SecurityJackson2Modules.enableDefaultTyping(mapper);
//        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);
//        return serializer;

        return new JdkSerializationRedisSerializer();
    }



}
