package rujianbin.autoconfiguration.springsecurity.config.handle.logout;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Created by rujianbin on 2017/12/15.
 */
public class RjbLogoutFilter extends LogoutFilter{

    public RjbLogoutFilter(String filterProcessesUrl,LogoutSuccessHandler logoutSuccessHandler, LogoutHandler... handlers){
        super(logoutSuccessHandler,handlers);
    }

}
