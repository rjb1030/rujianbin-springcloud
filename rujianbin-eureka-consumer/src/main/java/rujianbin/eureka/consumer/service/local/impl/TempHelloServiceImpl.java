package rujianbin.eureka.consumer.service.local.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rujianbin.eureka.consumer.service.local.ITempHelloService;

/**
 * Created by rujianbin on 2018/2/8.
 */
@Component
public class TempHelloServiceImpl implements ITempHelloService {

    @Autowired
    private RestTemplate template;

    @HystrixCommand(fallbackMethod = "helloFallback"
            ,commandProperties = {@HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="2000")}
            )
    @Override
    public String hello() {
        Long l1 = System.currentTimeMillis();
        String result = template.getForEntity("http://rujianbin-eureka-provider/hello",String.class).getBody();
        Long l2 = System.currentTimeMillis();
        return result+","+(l2-l1);
    }

    public String helloFallback(){
        return "error";
    }
}
