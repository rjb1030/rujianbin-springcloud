package rujianbin.app.websocket.chatroom.springwebsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;

/**
 * Created by rujianbin on 2018/1/19.
 */
public class RjbHttpSessionHandshakeInterceptor extends HttpSessionHandshakeInterceptor {

    private final Logger logger = LoggerFactory.getLogger(RjbHttpSessionHandshakeInterceptor.class);

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = ((ServletServerHttpRequest) request).getServletRequest().getParameter("token");
        String nickName = ((ServletServerHttpRequest) request).getServletRequest().getParameter("nickName");
        String userName = ((ServletServerHttpRequest) request).getServletRequest().getParameter("userName");
        if(StringUtils.isEmpty(token) || StringUtils.isEmpty(nickName) || StringUtils.isEmpty(userName)){
            logger.error("个人身份未上传！！！！ （本次测试是自己上传信息，实际应该上传token,后台根据token加载身份信息）");
            return false;
        }else{
            attributes.put("token",token);
            attributes.put("nickName",nickName);
            attributes.put("userName",userName);
            return super.beforeHandshake(request, response, wsHandler, attributes);
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
