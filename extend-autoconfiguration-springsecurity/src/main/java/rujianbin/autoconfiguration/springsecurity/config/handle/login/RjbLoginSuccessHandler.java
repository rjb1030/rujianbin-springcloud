package rujianbin.autoconfiguration.springsecurity.config.handle.login;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import rujianbin.security.principal.author.RjbSecurityUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by rujianbin on 2017/12/15.
 */
public class RjbLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public RjbLoginSuccessHandler(String defaultTargetUrl){
        super.setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException,
            ServletException {
        //获得授权后可得到用户信息   可使用SUserService进行数据库操作
        RjbSecurityUser rjbSecurityUser = (RjbSecurityUser)authentication.getPrincipal();
       /* Set<SysRole> roles = userDetails.getSysRoles();*/
        //输出登录提示信息
        System.out.println("用户 " + rjbSecurityUser.getName() + "，username="+rjbSecurityUser.getUsername()+" 登录");

        System.out.println("IP :"+getIpAddress(request));

        //个人信息放入session(redis)
        request.getSession().setAttribute(RjbSecurityUser.sessionKey,rjbSecurityUser);

        System.out.println("successHandle sessionId----->"+request.getSession().getId());
        super.onAuthenticationSuccess(request, response, authentication);
    }

    public String getIpAddress(HttpServletRequest request){
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}