package rujianbin.autoconfiguration.springsecurity.config.handle.login;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import rujianbin.security.principal.author.RjbWebAuthenticationDetails;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by xyl.
 */
public class RjbLoginForwardAuthenticationFailedHandler extends ForwardAuthenticationFailureHandler {


    public RjbLoginForwardAuthenticationFailedHandler(String forwardUrl){
        super(forwardUrl);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if(exception instanceof UsernameNotFoundException){
            System.out.println("登录失败：用户名不存在");
        }else if(exception instanceof BadCredentialsException){
            System.out.println("登录失败：密码错误");
        }else if(exception instanceof RjbWebAuthenticationDetails.KaptchaException){
            System.out.println("登录失败：验证码错误");
        }else if(exception instanceof SessionAuthenticationException){
            System.out.println("登录失败");
            exception.printStackTrace();
        }else{
            System.out.println("登录失败");
            exception.printStackTrace();
        }
        super.onAuthenticationFailure(request, response, exception);
    }

}
