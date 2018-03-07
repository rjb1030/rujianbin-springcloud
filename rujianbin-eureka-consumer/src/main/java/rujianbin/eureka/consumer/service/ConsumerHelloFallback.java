package rujianbin.eureka.consumer.service;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import rujianbin.eureka.api.bean.RjbParam;
import rujianbin.eureka.api.bean.UserDto;

/**
 * Created by rujianbin on 2018/2/12.
 */
@Component
public class ConsumerHelloFallback implements IConsumerHelloService {

    @Override
    public String header(@RequestHeader(value = "version", required = false) String version) {
        return "error header";
    }

    @Override
    public String hello(@RequestHeader(value = "version", required = false) String version) {
        return "error hello";
    }

    @Override
    public String say(@RequestParam("name") String name, @RequestParam("age") int age) {
        return "error say";
    }

    @Override
    public String talk(@RequestBody RjbParam param) {
        return "error talk";
    }

    @Override
    public UserDto sing(@RequestBody RjbParam param) {
        UserDto dto = new UserDto();
        dto.setName("error dto from sing");
        return dto;
    }
}
