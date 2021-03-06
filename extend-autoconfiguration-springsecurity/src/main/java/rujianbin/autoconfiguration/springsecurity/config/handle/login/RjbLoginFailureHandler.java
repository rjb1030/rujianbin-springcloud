package rujianbin.autoconfiguration.springsecurity.config.handle.login;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import rujianbin.security.principal.author.RjbWebAuthenticationDetails;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by rujianbin on 2017/12/15.
 */
public class RjbLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private String defaultFailureUrl;

    public RjbLoginFailureHandler(String defaultTargetUrl){
        this.defaultFailureUrl = defaultTargetUrl;
        super.setDefaultFailureUrl(defaultTargetUrl);
    }

    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(this.defaultFailureUrl == null) {
            this.logger.debug("No failure URL set, sending 401 Unauthorized error");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication Failed: " + exception.getMessage());
        } else {
            /**
             * //super.saveException(request, exception);
             * 如果session序列化是jackson,则Exception反序列化时会报is not whitelisted。解决方式是在Exception对象上加上jackson注解如@JsonAutoDetect
             * 异常不存入session则没有相关问题
             */
//            super.saveException(request, exception);
            String toUrl = this.defaultFailureUrl;
            if(exception instanceof UsernameNotFoundException){
                System.out.println("登录失败：用户名不存在");
                toUrl+="UsernameNotFoundException";
            }else if(exception instanceof BadCredentialsException){
                System.out.println("登录失败：密码错误");
                toUrl+="BadCredentialsException";
            }else if(exception instanceof RjbWebAuthenticationDetails.KaptchaException){
                System.out.println("登录失败：验证码错误");
                toUrl+="KaptchaException";
            }else if(exception instanceof SessionAuthenticationException){
                System.out.println("登录失败");
                exception.printStackTrace();
                toUrl+=exception.getMessage();
            }else{
                System.out.println("登录失败");
                exception.printStackTrace();
                toUrl+= exception.getMessage();
            }
            if(isUseForward()) {
                request.getRequestDispatcher(toUrl).forward(request, response);
            } else {
                getRedirectStrategy().sendRedirect(request, response, toUrl);
            }
        }

    }

}
