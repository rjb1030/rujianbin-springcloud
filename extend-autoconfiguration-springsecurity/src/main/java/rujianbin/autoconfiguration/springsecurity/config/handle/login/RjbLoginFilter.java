package rujianbin.autoconfiguration.springsecurity.config.handle.login;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import rujianbin.common.utils.RSAUtils;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.interfaces.RSAPrivateKey;

/**
 * Created by rujianbin on 2017/12/14.
 */
public class RjbLoginFilter extends UsernamePasswordAuthenticationFilter {

    private boolean postOnly = false;


    public RjbLoginFilter(){
        super();
    }

    public RjbLoginFilter(String filterProcessesUrl,AuthenticationSuccessHandler successHandler,AuthenticationFailureHandler failureHandler,AuthenticationManager authenticationManager,AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource){
        super.setFilterProcessesUrl(filterProcessesUrl);
        super.setAuthenticationSuccessHandler(successHandler);
        super.setAuthenticationFailureHandler(failureHandler);
        super.setAuthenticationManager(authenticationManager);
        super.setAuthenticationDetailsSource(authenticationDetailsSource);

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        HttpSession session = request.getSession();
        RSAPrivateKey privateKey = (RSAPrivateKey) session.getAttribute(RSAUtils.PRIVATE_KEY_SESSION_ATTRIBUTE_NAME);
        if (privateKey != null && !StringUtils.isEmpty(password)) {
            password = RSAUtils.decrypt(privateKey, password);
            session.removeAttribute(RSAUtils.PRIVATE_KEY_SESSION_ATTRIBUTE_NAME);
        }
        username = username.trim();

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

        // Allow subclasses to set the "details" property
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    public boolean isPostOnly() {
        return postOnly;
    }

    @Override
    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
        super.setPostOnly(postOnly);
    }

    @Override
    public void setFilterProcessesUrl(String filterProcessesUrl) {
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(filterProcessesUrl,"POST"));
    }
}
