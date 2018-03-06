package rujianbin.eureka.consumer.config;

import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rujianbin on 2018/2/28.
 */

@Configuration
public class FeignConfig {

    /**
     * logging.level.<FeignClient>=DEBUG 开启debu FeignClient是feign接口的全路径
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
     * Feign请求拦截器
     * 如果扫描成bean则全局生效，如果@FeignClient配置configuration=MyFeignRequestInterceptor.class则制定client生效
     * 隔离策略是SEMAPHORE时  controller和RequestInterceptor在一个线程中，threadlocal可以传递header
     * 隔离策略是Thread时,feign的http请求是在新的线程中的，threadlocal就不同了
     * @author yinjihuan
     * @create 2017-11-10 17:25
     **/
    @Component
    public static class MyFeignRequestInterceptor  implements RequestInterceptor {

        public MyFeignRequestInterceptor() {

        }

        @Override
        public void apply(RequestTemplate template) {
            System.out.println(Thread.currentThread().getId());
            String name = (String)RjbThreadLocal.threadLocal.get();
            template.header("RequestInterceptor  XXX-PP", name);
        }
    }



}
