package rujianbin.app.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import rujianbin.app.web.entity.Person;
import rujianbin.app.web.mapper.TestMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
}
