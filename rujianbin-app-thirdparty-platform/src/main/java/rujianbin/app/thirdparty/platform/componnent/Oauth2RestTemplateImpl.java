package rujianbin.app.thirdparty.platform.componnent;


import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import org.apache.http.Consts;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import rujianbin.app.thirdparty.platform.config.OauthClientProperties;

import java.io.IOException;
import java.util.*;

/**
 * Created by rujianbin@.com on 2017/5/9.
 */
@Component
@EnableConfigurationProperties(OauthClientProperties.class)
public class Oauth2RestTemplateImpl implements IOAuth2RestTemplate {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String split = "[,]|[ ]|[;]";

    private volatile AuthorizationCodeResourceDetails details;

    @Autowired
    private OauthClientProperties oauthClientProperties;



    /**
     * 获取restTemplate
     * @return
     */
    @Override
    public OAuth2RestTemplate getTemplate(String code){
        AuthorizationCodeResourceDetails resourceDetails = getDetails();
        OAuth2AccessToken token = getToken(code);
        OAuth2RestTemplate template = new OAuth2RestTemplate(resourceDetails, new DefaultOAuth2ClientContext(token));
        Long currentTimeMillis = System.currentTimeMillis();
        String sign = getSign(currentTimeMillis);
        filterHeader(template,sign,currentTimeMillis);
        return template;
    }

    /**
     * 获取ResourceDetails对象
     * @return
     */
    private AuthorizationCodeResourceDetails getDetails(){
        if(details!=null){
            return details;
        }else{
            synchronized (Oauth2RestTemplateImpl.class){
                if(details==null){
                    details = new AuthorizationCodeResourceDetails();
                    details.setAccessTokenUri(oauthClientProperties.getAccessTokenUri());
                    details.setClientId(oauthClientProperties.getClientId());
                    details.setClientSecret(oauthClientProperties.getClientSecret());
                    details.setScope(Lists.newArrayList(oauthClientProperties.getScope().split(split)));
                    return details;
                }else{
                    return details;
                }
            }
        }
    }


    /**
     * 获取token
     * @return
     */
    private OAuth2AccessToken getToken(String code){
        AuthorizationCodeResourceDetails resourceDetails = getDetails();
        AuthorizationCodeAccessTokenProvider provider = new AuthorizationCodeAccessTokenProvider();
        provider.setStateMandatory(false);
        DefaultAccessTokenRequest request = new DefaultAccessTokenRequest();
        request.setAuthorizationCode(code);
        request.set("redirect_uri",oauthClientProperties.getRedirectUri());
        OAuth2AccessToken accessToken = provider.obtainAccessToken(resourceDetails,request);
        logger.info("oauth2 get token: "+accessToken.getValue());
        return accessToken;
    }



    /**
     * 获取签名
     * @return
     */
    private String getSign(Long currentTimeMillis){
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<BasicNameValuePair>();
        basicNameValuePairs.add(new BasicNameValuePair("appSecret",oauthClientProperties.getClientSecret()));
        basicNameValuePairs.add(new BasicNameValuePair("sendTime",String.valueOf(currentTimeMillis)));
        Collections.sort(basicNameValuePairs, new Comparator<BasicNameValuePair>() {
            public int compare(BasicNameValuePair o1, BasicNameValuePair o2) {
                return o1.getName().compareTo(o2.getName());
            }

            @Override
            public int hashCode() {
                return super.hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                return String.valueOf(this).equals(String.valueOf(obj));
            }
        });

        String str = URLEncodedUtils.format(basicNameValuePairs, Consts.UTF_8);
        return Hashing.md5().hashBytes(str.getBytes()).toString();
    }

    /**
     * 设置请求头
     * @param template
     * @param sign
     * @param currentTimeMillis
     */
    private void filterHeader(OAuth2RestTemplate template,final String sign,final Long currentTimeMillis){
        template.setInterceptors(Arrays.<ClientHttpRequestInterceptor>asList(new ClientHttpRequestInterceptor(){
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                    throws IOException {
                request.getHeaders().add("Content-type", "application/json;charset=UTF-8");
                request.getHeaders().add("sign", sign);
                request.getHeaders().add("sendTime",String.valueOf(currentTimeMillis));
                return execution.execute(request, body);
            }
        }));
    }


}
