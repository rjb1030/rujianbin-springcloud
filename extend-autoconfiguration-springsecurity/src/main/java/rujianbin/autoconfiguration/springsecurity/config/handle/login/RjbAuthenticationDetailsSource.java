package rujianbin.autoconfiguration.springsecurity.config.handle.login;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import rujianbin.security.principal.author.RjbWebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rujianbin on 2017/12/15.
 */
public class RjbAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new RjbWebAuthenticationDetails(context);
    }

}
