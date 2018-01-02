package rujianbin.oauth2.resource.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rujianbin on 2017/12/29.
 */

@Controller
@RequestMapping("/oauth2/api")
public class ResourceController {

    @ResponseBody
    @RequestMapping("/resource1")
    public Map<String,Object> resource1(){
        Map<String,Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("message","hello");
        return map;
    }
}
