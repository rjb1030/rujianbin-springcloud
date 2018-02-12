package rujianbin.eureka.consumer.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import rujianbin.eureka.api.service.IHelloService;

/**
 * Created by rujianbin on 2018/2/7.
 *
 * 利用@FeignClient 以接口形式封装服务提供者的restFul调用
 */

@FeignClient(value = "rujianbin-eureka-provider",fallback = ConsumerHelloFallback.class)
public interface IConsumerHelloService extends IHelloService {


}
