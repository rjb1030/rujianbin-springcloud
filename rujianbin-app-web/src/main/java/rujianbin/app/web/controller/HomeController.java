package rujianbin.app.web.controller;


import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rujianbin.security.principal.author.RjbSecurityUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by 汝建斌 on 2017/4/10.
 */
@Controller
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("")
    public String login(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        Object obj = request.getSession().getAttribute(RjbSecurityUser.sessionKey);
        if(obj!=null){
            User rjbSecurityUser  = (User)obj;
            model.put("user",rjbSecurityUser.getUsername()+"("+rjbSecurityUser.getUsername()+")");
            model.put("authority",rjbSecurityUser.getAuthorities());
        }

        System.out.println("sessionId----->"+request.getSession().getId());
        return "home/home";
    }


    @RequestMapping("/hello")
    @ResponseBody
    public String login(HttpServletRequest request, HttpServletResponse response) {
        return "hello world! you are authorization";
    }


    @RequestMapping("/chat-room")
    public String chatRoom(HttpServletRequest request, HttpServletResponse response,ModelMap model) {
        return "websocket/chat-room";
    }


}
