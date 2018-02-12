package rujianbin.eureka.zuul.apigateway.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rujianbin on 2018/2/12.
 */
@Configuration
public class RjbFilterConfig {

    @Bean
    public RjbZuulFilter rjbZuulFilter(){
        return new RjbZuulFilter();
    }
}
