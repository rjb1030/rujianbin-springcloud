package rujianbin.oauth2.authorization.server.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rujianbin.security.principal.author.RjbSecurityUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by rujianbin on 2018/1/2.
 */

@Controller
@RequestMapping("/oauth2/principal")
public class OauthPrincipalController {

    @RequestMapping("currentUser")
    @ResponseBody
    public RjbSecurityUser currentUser(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
//        RjbSecurityUser rjbSecurityUser = (RjbSecurityUser)request.getSession().getAttribute(RjbSecurityUser.sessionKey);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getPrincipal() instanceof RjbSecurityUser){
            return (RjbSecurityUser)authentication.getPrincipal();
        }else{
            return null;
        }

    }

}
