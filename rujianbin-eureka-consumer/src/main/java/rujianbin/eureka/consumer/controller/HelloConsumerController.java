package rujianbin.eureka.consumer.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import rujianbin.common.utils.RjbStringUtils;
import rujianbin.eureka.api.bean.RjbParam;
import rujianbin.eureka.api.bean.UserDto;
import rujianbin.eureka.consumer.service.IConsumerHelloService;
import rujianbin.eureka.consumer.service.local.ITempHelloService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by rujianbin on 2018/2/6.
 */

@RefreshScope  //该注解允许配置中心参数更新后调用/refresh后热更新变量
@RestController
public class HelloConsumerController {

    @Value("${rujianbin.testName}")
    private String testName;

    @Autowired
    private ITempHelloService tempHelloService;

    @Autowired
    private IConsumerHelloService helloService;

    @RequestMapping("/hello-consumer")
    public String hello(){

        for(int i=0;i<15;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Long s1 = System.currentTimeMillis();
                    String ss = tempHelloService.hello();
                    Long s2 = System.currentTimeMillis();
                    System.out.println(ss+"，耗时="+(s2-s1));
                }
            }).start();
        }
        return "请查看后台输出";
    }

    @RequestMapping("/hello-consumer2")
    public Map hello2(){
        Long s1 = System.currentTimeMillis();
        String hello = helloService.hello();
        String say = helloService.say("张三",12);
        RjbParam param = new RjbParam();
        param.setSearchName("王五");
        param.setSearchPageNo(23);
        param.setProductId(Lists.newArrayList(12L,13L,14L));
        String talk = helloService.talk(param);

        UserDto sing = helloService.sing(param);

        Map<String,Object> map = new LinkedHashMap<>();
        map.put("hello",hello);
        map.put("say",say);
        map.put("talk",talk);
        map.put("sing",sing);
        map.put("testName",testName);
        Long s2 = System.currentTimeMillis();
        map.put("耗时",s2-s1);
        System.out.println(RjbStringUtils.ObjectToString(map));
        return map;
    }
}
