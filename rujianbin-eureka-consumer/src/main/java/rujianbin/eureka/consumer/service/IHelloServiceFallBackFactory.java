package rujianbin.eureka.consumer.service;

import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rujianbin.eureka.api.service.IHelloService;

/**
 * Created by rujianbin on 2018/3/7.
 */
@Component
public class IHelloServiceFallBackFactory implements FallbackFactory<IHelloService> {

    private static final Logger log = LoggerFactory.getLogger(IHelloServiceFallBackFactory.class);


    @Override
    public IHelloService create(Throwable throwable) {
        log.error("【ERROR】服务降级原因: {} " ,throwable.getMessage());

        return new ConsumerHelloFallback();

    }
}
