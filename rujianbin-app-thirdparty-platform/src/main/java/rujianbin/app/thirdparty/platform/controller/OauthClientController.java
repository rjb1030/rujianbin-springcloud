package rujianbin.app.thirdparty.platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rujianbin.app.thirdparty.platform.componnent.IOAuth2RestTemplate;
import rujianbin.app.thirdparty.platform.config.OauthClientProperties;
import rujianbin.security.principal.entity.UserEntity;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rujianbin on 2018/1/2.
 */
@Controller
@RequestMapping("/oauth2/client")
public class OauthClientController {

    @Autowired
    private IOAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    private OauthClientProperties oauthClientProperties;

    /**
     * 请求code
     * 带上session的cookie 请求oauth/authorize。  授权服务器要根据cookie加载登录信息session 然后返回将code后缀到redirectUrl并请求redirectUrl
     * code授权必须是有正常的security登录session，故授权服务也得配置session持久化(同应用服务器)，故而可以通过cookie加载session
     * @param request
     * @param response
     * @param model
     * @return
     */
    public String authorize(HttpServletRequest request, HttpServletResponse response, ModelMap model){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for(Cookie cookie : cookies){
                System.out.println("cookie name="+cookie.getName()+",value="+cookie.getValue()+",domain="+cookie.getDomain());
            }
        }
        model.put("client_id",oauthClientProperties.getClientId());
        model.put("grant_type","authorization_code");
        //这个跳转地址要和oauth_client_details的对应client的web_server_direct_uri一致
        model.put("redirect_uri",oauthClientProperties.getRedirectUri());
        model.put("response_type","code");
        return "redirect:"+oauthClientProperties.getAuthorizeUrl();
    }


    /**
     * redirect url 可以返回页面或者json
     * @return
     */
    @RequestMapping("/currentOauthUser")
    @ResponseBody
    public Map<String,Object> getCurrentOauthUser(HttpServletRequest request) throws Exception{
        String code = request.getParameter("code");
        System.out.println("oauth 请求到的code--->"+code);


        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isEmpty(code)){
            map.put("userinfo","拒绝授权");
        }else{
            OAuth2RestTemplate template = oAuth2RestTemplate.getTemplate(code);
            List<UserEntity> userList = getForObjectList(template,"",new ParameterizedTypeReference<List<UserEntity>>(){});
            List<UserEntity> userList2 = getForObjectList(template,"",new ParameterizedTypeReference<List<UserEntity>>(){});
            map.put("userinfo",new ObjectMapper().writeValueAsString(userList));
        }
        return null;
    }

    private  <T> List<T> getForObjectList(OAuth2RestTemplate template,String url, ParameterizedTypeReference<List<T>> typeRef){
        ResponseEntity<List<T>> result = template.exchange(url,
                HttpMethod.GET,
                new HttpEntity<String>(new HttpHeaders()),
                typeRef);
        return result.getBody();
    }
}
