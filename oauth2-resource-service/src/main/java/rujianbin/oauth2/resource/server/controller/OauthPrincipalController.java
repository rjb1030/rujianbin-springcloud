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
import java.util.List;

/**
 * Created by rujianbin on 2018/1/2.
 */

@Controller
@RequestMapping("/oauth2/principal")
public class OauthPrincipalController {

    @RequestMapping("currentUser")
    @ResponseBody
    public List<UserEntity> currentUser(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity current = null;
        if (authentication.getPrincipal() instanceof RjbSecurityUser) {
            RjbSecurityUser rjbSecurityUser = (RjbSecurityUser) authentication.getPrincipal();
            current = new UserEntity();
            current.setName(rjbSecurityUser.getName());
            current.setUsername(rjbSecurityUser.getUsername());
            current.setAuthorityEntityList(rjbSecurityUser.getAuthorityEntityList());
        } else {
            //远程验证token时 返回的authentication是转换过的
            current = new UserEntity();
            current.setUsername((String) authentication.getPrincipal());
            List<AuthorityEntity> list = Lists.newArrayList();
            current.setAuthorityEntityList(list);
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                list.add(new AuthorityEntity(authority.getAuthority()));
            }
        }
        List list = Lists.newArrayList();
        if(current!=null){
            list.add(current);
        }
        return list;

    }

}
