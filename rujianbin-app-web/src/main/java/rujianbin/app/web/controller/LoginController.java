package rujianbin.app.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import rujianbin.app.web.entity.Person;
import rujianbin.app.web.mapper.TestMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 汝建斌 on 2017/4/10.
 */

@Controller
@RequestMapping("/login")
public class LoginController {




    //http://127.0.0.1:7030/rujianbin-app-web/login
    @RequestMapping("")
    public String login(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("sessionId----->"+request.getSession().getId());
        return "login/login";
    }

    @RequestMapping(value = "/logout",method = {RequestMethod.POST,RequestMethod.GET})
    public String logout(ModelMap model) {
        System.out.println("成功退出。。。。");
        return null;
    }

    @RequestMapping(value = "/login-noAuth",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map<String,String> noAuth(HttpServletRequest request){
        Map<String,String> map = new HashMap<>();
        map.put("success","false");
        map.put("message","登录已过期或未登录");
        map.put("data","");
        return map;
    }


    @RequestMapping(value = "/login-error",method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map<String,String> error(HttpServletRequest request,HttpServletResponse response){
        AuthenticationException exception = (AuthenticationException)request.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
        Map<String,String> map = new HashMap<>();
        map.put("success","false");
        map.put("message",exception.getMessage());
        map.put("data","");
        return map;
    }
}
