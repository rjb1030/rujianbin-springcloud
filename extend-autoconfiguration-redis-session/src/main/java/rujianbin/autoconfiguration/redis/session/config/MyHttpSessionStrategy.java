package rujianbin.autoconfiguration.redis.session.config;

import org.springframework.session.Session;
import org.springframework.session.web.http.*;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xyl.
 */
public class MyHttpSessionStrategy implements MultiHttpSessionStrategy, HttpSessionManager {

    private static final String DEFAULT_DELIMITER = " ";
    private static final String SESSION_IDS_WRITTEN_ATTR = CookieHttpSessionStrategy.class.getName().concat(".SESSIONS_WRITTEN_ATTR");
    static final String DEFAULT_ALIAS = "0";
    static final String DEFAULT_SESSION_ALIAS_PARAM_NAME = "_s";
    private static final Pattern ALIAS_PATTERN = Pattern.compile("^[\\w-]{1,50}$");
    private String sessionParam = "_s";
    @Resource(name="rjbCookieSerializer")
    private CookieSerializer cookieSerializer;
    private String deserializationDelimiter = " ";
    private String serializationDelimiter = " ";

    private static String tokenName = "_token";

    public MyHttpSessionStrategy(){

    }

    public MyHttpSessionStrategy(String tokenName){
        this.tokenName = tokenName;
    }


    public String getRequestedSessionId(HttpServletRequest request) {
        String sessionId = request.getParameter(tokenName);
        if(sessionId==null){
            sessionId = request.getHeader(tokenName);
        }
        if(sessionId==null){
            Map sessionIds = this.getSessionIds(request);
            String sessionAlias = this.getCurrentSessionAlias(request);
            sessionId =  (String)sessionIds.get(sessionAlias);
        }
       return sessionId;
    }

    public String getCurrentSessionAlias(HttpServletRequest request) {
        if(this.sessionParam == null) {
            return "0";
        } else {
            String u = request.getParameter(this.sessionParam);
            return u == null?"0":(!ALIAS_PATTERN.matcher(u).matches()?"0":u);
        }
    }

    public String getNewSessionAlias(HttpServletRequest request) {
        Set sessionAliases = this.getSessionIds(request).keySet();
        if(sessionAliases.isEmpty()) {
            return "0";
        } else {
            long lastAlias = Long.decode("0").longValue();
            Iterator var5 = sessionAliases.iterator();

            while(var5.hasNext()) {
                String alias = (String)var5.next();
                long selectedAlias = this.safeParse(alias);
                if(selectedAlias > lastAlias) {
                    lastAlias = selectedAlias;
                }
            }

            return Long.toHexString(lastAlias + 1L);
        }
    }

    private long safeParse(String hex) {
        try {
            return Long.decode("0x" + hex).longValue();
        } catch (NumberFormatException var3) {
            return 0L;
        }
    }

    public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
        Set sessionIdsWritten = this.getSessionIdsWritten(request);
        if(!sessionIdsWritten.contains(session.getId())) {
            sessionIdsWritten.add(session.getId());
            Map sessionIds = this.getSessionIds(request);
            String sessionAlias = this.getCurrentSessionAlias(request);
            sessionIds.put(sessionAlias, session.getId());
            String cookieValue = this.createSessionCookieValue(sessionIds);
            this.cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, cookieValue));
        }
    }

    private Set<String> getSessionIdsWritten(HttpServletRequest request) {
        Object sessionsWritten = (Set)request.getAttribute(SESSION_IDS_WRITTEN_ATTR);
        if(sessionsWritten == null) {
            sessionsWritten = new HashSet();
            request.setAttribute(SESSION_IDS_WRITTEN_ATTR, sessionsWritten);
        }

        return (Set)sessionsWritten;
    }

    private String createSessionCookieValue(Map<String, String> sessionIds) {
        if(sessionIds.isEmpty()) {
            return "";
        } else if(sessionIds.size() == 1 && sessionIds.keySet().contains("0")) {
            return (String)sessionIds.values().iterator().next();
        } else {
            StringBuffer buffer = new StringBuffer();
            Iterator var3 = sessionIds.entrySet().iterator();

            while(var3.hasNext()) {
                Map.Entry entry = (Map.Entry)var3.next();
                String alias = (String)entry.getKey();
                String id = (String)entry.getValue();
                buffer.append(alias);
                buffer.append(this.serializationDelimiter);
                buffer.append(id);
                buffer.append(this.serializationDelimiter);
            }

            buffer.deleteCharAt(buffer.length() - 1);
            return buffer.toString();
        }
    }

    public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
        Map sessionIds = this.getSessionIds(request);
        String requestedAlias = this.getCurrentSessionAlias(request);
        sessionIds.remove(requestedAlias);
        String cookieValue = this.createSessionCookieValue(sessionIds);
        this.cookieSerializer.writeCookieValue(new CookieSerializer.CookieValue(request, response, cookieValue));
    }

    public void setSessionAliasParamName(String sessionAliasParamName) {
        this.sessionParam = sessionAliasParamName;
    }

    public void setCookieSerializer(CookieSerializer cookieSerializer) {
        Assert.notNull(cookieSerializer, "cookieSerializer cannot be null");
        this.cookieSerializer = cookieSerializer;
    }

    /** @deprecated */
    @Deprecated
    public void setCookieName(String cookieName) {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName(cookieName);
        this.cookieSerializer = serializer;
    }

    public void setDeserializationDelimiter(String delimiter) {
        this.deserializationDelimiter = delimiter;
    }

    public void setSerializationDelimiter(String delimiter) {
        this.serializationDelimiter = delimiter;
    }

    public Map<String, String> getSessionIds(HttpServletRequest request) {
        List cookieValues = this.cookieSerializer.readCookieValues(request);
        String sessionCookieValue = cookieValues.isEmpty()?"":(String)cookieValues.iterator().next();
        LinkedHashMap result = new LinkedHashMap();
        StringTokenizer tokens = new StringTokenizer(sessionCookieValue, this.deserializationDelimiter);
        if(tokens.countTokens() == 1) {
            result.put("0", tokens.nextToken());
            return result;
        } else {
            while(tokens.hasMoreTokens()) {
                String alias = tokens.nextToken();
                if(!tokens.hasMoreTokens()) {
                    break;
                }

                String id = tokens.nextToken();
                result.put(alias, id);
            }

            return result;
        }
    }

    public HttpServletRequest wrapRequest(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(HttpSessionManager.class.getName(), this);
        return request;
    }

    public HttpServletResponse wrapResponse(HttpServletRequest request, HttpServletResponse response) {
        return new MyHttpSessionStrategy.MultiSessionHttpServletResponse(response, request);
    }

    public String encodeURL(String url, String sessionAlias) {
        String encodedSessionAlias = this.urlEncode(sessionAlias);
        int queryStart = url.indexOf("?");
        boolean isDefaultAlias = "0".equals(encodedSessionAlias);
        if(queryStart < 0) {
            return isDefaultAlias?url:url + "?" + this.sessionParam + "=" + encodedSessionAlias;
        } else {
            String path = url.substring(0, queryStart);
            String query = url.substring(queryStart + 1, url.length());
            String replacement = isDefaultAlias?"":"$1" + encodedSessionAlias;
            query = query.replaceFirst("((^|&)" + this.sessionParam + "=)([^&]+)?", replacement);
            String sessionParamReplacement = String.format("%s=%s", new Object[]{this.sessionParam, encodedSessionAlias});
            if(!isDefaultAlias && !query.contains(sessionParamReplacement) && url.endsWith(query)) {
                if(!query.endsWith("&") && query.length() != 0) {
                    query = query + "&";
                }

                query = query + sessionParamReplacement;
            }

            return path + "?" + query;
        }
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException(var3);
        }
    }

    class MultiSessionHttpServletResponse extends HttpServletResponseWrapper {
        private final HttpServletRequest request;

        MultiSessionHttpServletResponse(HttpServletResponse response, HttpServletRequest request) {
            super(response);
            this.request = request;
        }

        private String getCurrentSessionAliasFromUrl(String url) {
            String currentSessionAliasFromUrl = null;
            int queryStart = url.indexOf("?");
            if (queryStart >= 0) {
                String query = url.substring(queryStart + 1);
                Matcher matcher = Pattern.compile(String.format("%s=([^&]+)", new Object[]{MyHttpSessionStrategy.this.sessionParam})).matcher(query);
                if (matcher.find()) {
                    currentSessionAliasFromUrl = matcher.group(1);
                }
            }

            return currentSessionAliasFromUrl;
        }

        public String encodeRedirectURL(String url) {
            String encodedUrl = super.encodeRedirectURL(url);
            String currentSessionAliasFromUrl = this.getCurrentSessionAliasFromUrl(encodedUrl);
            String alias = currentSessionAliasFromUrl != null ? currentSessionAliasFromUrl : MyHttpSessionStrategy.this.getCurrentSessionAlias(this.request);
            return MyHttpSessionStrategy.this.encodeURL(encodedUrl, alias);
        }

        public String encodeURL(String url) {
            String encodedUrl = super.encodeURL(url);
            String currentSessionAliasFromUrl = this.getCurrentSessionAliasFromUrl(encodedUrl);
            String alias = currentSessionAliasFromUrl != null ? currentSessionAliasFromUrl : MyHttpSessionStrategy.this.getCurrentSessionAlias(this.request);
            return MyHttpSessionStrategy.this.encodeURL(encodedUrl, alias);
        }
    }
}
