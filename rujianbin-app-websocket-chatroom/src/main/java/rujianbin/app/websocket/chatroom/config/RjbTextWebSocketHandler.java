package rujianbin.app.websocket.chatroom.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import rujianbin.common.utils.RjbStringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rujianbin on 2018/1/18.
 */
public class RjbTextWebSocketHandler extends TextWebSocketHandler {



    private static AtomicInteger onlineCount = new AtomicInteger(0);


    private static ConcurrentHashMap<String,RjbWebsocketUser> webSocketUserMap = new ConcurrentHashMap<>();


    /**
     * socket连接
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String,Object> attributes = session.getAttributes();
        RjbWebsocketUser newUser = new RjbWebsocketUser((String)attributes.get("userName"),(String)attributes.get("nickName"),session);
        webSocketUserMap.put(session.getId(),newUser);
        int count = onlineCount.incrementAndGet();
        System.out.println("有新连接加入！当前在线人数为" + count);
        /**
         * 广播通知 有新人加入
         */
        for(Map.Entry<String,RjbWebsocketUser> entry : webSocketUserMap.entrySet()){
            RjbWebsocketUser user = entry.getValue();
            user.getSession().sendMessage(new TextMessage(assembleTextMessage("系统",newUser.getNickName()+"加入聊天室")));
        }
    }


    /**
     * socket关闭
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        RjbWebsocketUser currentUser = webSocketUserMap.get(session.getId());
        webSocketUserMap.remove(session.getId());
        int count = onlineCount.decrementAndGet();
        System.out.println("有新连接加入！当前在线人数为" + count);
        /**
         * 广播通知 有新人加入
         */
        for(Map.Entry<String,RjbWebsocketUser> entry : webSocketUserMap.entrySet()){
            RjbWebsocketUser user = entry.getValue();
            user.getSession().sendMessage(new TextMessage(assembleTextMessage("系统",currentUser.getNickName()+"离开聊天室")));
        }

    }

    /**
     * socket异常
     * @param session
     * @param exception
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("异常打印");
        exception.printStackTrace();
    }


    /**
     * 接收消息，处理消息
     * @param session
     * @param message
     * @throws Exception
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        String currentSessionId = session.getId();
        RjbWebsocketUser currentUser = webSocketUserMap.get(currentSessionId);
        System.out.println("来自客户端("+currentUser.getNickName()+")的消息:" + msg);

        //群发消息
        for(Map.Entry<String,RjbWebsocketUser> entry : webSocketUserMap.entrySet()){
            String sessionId = entry.getKey();
            RjbWebsocketUser user = entry.getValue();
            try {
                if(!sessionId.equals(currentSessionId)){
                    user.getSession().sendMessage(new TextMessage(assembleTextMessage(currentUser.getNickName(),msg)));
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }


    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
    }


    private String assembleTextMessage(String from,String content){
        RjbMessage msg = new RjbMessage();
        msg.setFrom(from);
        msg.setContent(content);
        return RjbStringUtils.ObjectToString(msg);
    }

}
