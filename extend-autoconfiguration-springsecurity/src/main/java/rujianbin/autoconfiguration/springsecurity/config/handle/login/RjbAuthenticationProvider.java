package rujianbin.autoconfiguration.springsecurity.config.handle.login;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import rujianbin.security.principal.author.RjbWebAuthenticationDetails;

/**
 * Created by 汝建斌 on 2017/4/11.
 */
public class RjbAuthenticationProvider extends DaoAuthenticationProvider {

    public RjbAuthenticationProvider(boolean hideUserNotFoundExceptions){
        super.setHideUserNotFoundExceptions(hideUserNotFoundExceptions);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
        RjbWebAuthenticationDetails details = (RjbWebAuthenticationDetails)authentication.getDetails();
        System.out.println("客户自定义数据-验证码："+details.getCaptcha());
        return super.authenticate(authentication);
    }

    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Override
    protected UserDetailsService getUserDetailsService() {
        return super.getUserDetailsService();
    }

}
