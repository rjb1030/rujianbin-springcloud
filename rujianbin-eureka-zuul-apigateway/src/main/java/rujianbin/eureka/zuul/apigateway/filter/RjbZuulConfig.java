package rujianbin.eureka.zuul.apigateway.filter;

import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rujianbin on 2018/2/12.
 */
@Configuration
public class RjbZuulConfig {

    /**
     * filter创建
     * @return
     */
    @Bean
    public RjbZuulFilter rjbZuulFilter(){
        return new RjbZuulFilter();
    }

//    /**
//     * 自定义路由匹配规则（服务名和path的映射规则），用以对版本号服务路径的方便管理
//     * 比如服务名xxxxService-v1映射到/v1/xxxxService为path的路由
//     * 系统优先使用自定义构建的匹配规则，没有匹配则会按照默认的匹配规则即完整服务名作为前缀/xxxxService-v1
//     * @return
//     */
//    @Bean
//    public PatternServiceRouteMapper serviceRouteMapper(){
//        return new PatternServiceRouteMapper("(?<name>^.+)-(?<version>v.+$)","${version}/${name}");
//    }
}
