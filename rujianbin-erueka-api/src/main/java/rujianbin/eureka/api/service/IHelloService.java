package rujianbin.eureka.api.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rujianbin.eureka.api.bean.RjbParam;
import rujianbin.eureka.api.bean.UserDto;

/**
 * Created by rujianbin on 2018/2/7.
 */
public interface IHelloService {

    @RequestMapping("/hello")
    String hello();

    @RequestMapping("/say")
    String say(@RequestParam("name") String name, @RequestParam("age") int age);

    @RequestMapping("/talk")
    String talk(@RequestBody RjbParam param);

    @RequestMapping("/sing")
    UserDto sing(@RequestBody RjbParam param);


}
