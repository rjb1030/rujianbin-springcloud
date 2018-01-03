package rujianbin.oauth2.resource.server.controller;

import com.google.common.collect.Lists;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rujianbin.security.principal.author.RjbSecurityUser;
import rujianbin.security.principal.entity.AuthorityEntity;
import rujianbin.security.principal.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
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
