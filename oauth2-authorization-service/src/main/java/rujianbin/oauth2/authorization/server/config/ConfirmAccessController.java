package rujianbin.oauth2.authorization.server.config;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rujianbin on 2018/1/3.
 */
@Controller
@RequestMapping
@SessionAttributes("authorizationRequest")
public class ConfirmAccessController {



    @RequestMapping("/oauth2/confirm_access")
    public String getAccessConfirmation(Map<String, Object> model, HttpServletRequest request){
        model.put("title","我的授权协议");
        return "confirm_approve";
    }

    @RequestMapping("/oauth2/error")
    public String handleError(HttpServletRequest request, HttpServletResponse response,Map<String, Object> model) throws Exception {
        // We can add more stuff to the model here for JSP rendering.  If the client was a machine then
        // the JSON will already have been rendered.

        Object error = request.getAttribute("error");

        if (error instanceof OAuth2Exception) {
            OAuth2Exception oauthError = (OAuth2Exception) error;
            model.put(OAuth2Exception.ERROR, oauthError.getOAuth2ErrorCode());
            model.put(OAuth2Exception.DESCRIPTION, oauthError.getMessage());
            response.setStatus(oauthError.getHttpErrorCode());
        }
        else {
            model.put(OAuth2Exception.ERROR, "Unkonw Error");
            model.put(OAuth2Exception.DESCRIPTION, "Unkonw Error");
        }
        model.put("message", "There was a problem with the OAuth2 protocol");
        return "confirm_error";
    }
}
