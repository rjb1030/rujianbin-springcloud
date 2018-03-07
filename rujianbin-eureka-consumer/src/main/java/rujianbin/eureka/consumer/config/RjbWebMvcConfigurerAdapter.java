package rujianbin.eureka.consumer.config;

import org.springframework.cloud.context.scope.thread.ThreadLocalScopeCache;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by rujianbin on 2018/2/28.
 */
@Configuration
public class RjbWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(new SpringMVCInterceptor());
        // 拦截配置
        addInterceptor.addPathPatterns("/**");
    }


    public static class SpringMVCInterceptor  extends HandlerInterceptorAdapter {

        @Override
        public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
            String name = httpServletRequest.getHeader("XXX-PP");
            System.out.println("controller请求中 header[XXX-PP]="+name);
            RjbThreadLocal.threadLocal.set(name);
            System.out.println("线程id="+Thread.currentThread().getId());
            return true;
        }
    }
}
