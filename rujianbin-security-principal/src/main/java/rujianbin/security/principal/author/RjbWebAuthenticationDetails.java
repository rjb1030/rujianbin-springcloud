package rujianbin.security.principal.author;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.code.kaptcha.Constants;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 汝建斌 on 2017/4/11.
 */
@JsonAutoDetect
public class RjbWebAuthenticationDetails extends WebAuthenticationDetails {

    private String Captcha;

    public RjbWebAuthenticationDetails(HttpServletRequest request){
        super(request);
        Captcha = request.getParameter("vCode");
        if(Captcha==null || !Captcha.equals(request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY))){
            throw new KaptchaException("Captcha code error");
        }
    }

    public String getCaptcha() {
        return Captcha;
    }

    public void setCaptcha(String captcha) {
        Captcha = captcha;
    }

    public static class KaptchaException extends AuthenticationException {

        public KaptchaException(String msg, Throwable t) {
            super(msg, t);
        }

        public KaptchaException(String msg) {
            super(msg);
        }
    }
}
