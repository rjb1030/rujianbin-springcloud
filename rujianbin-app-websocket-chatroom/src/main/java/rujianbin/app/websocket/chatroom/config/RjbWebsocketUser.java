package rujianbin.app.websocket.chatroom.config;

import org.springframework.web.socket.WebSocketSession;

/**
 * Created by rujianbin on 2018/1/18.
 */
public class RjbWebsocketUser {

    private WebSocketSession session;

    private String userName;

    private String nickName;

    public RjbWebsocketUser(){}

    public RjbWebsocketUser(String userName,String nickName,WebSocketSession session){
        this.userName=userName;
        this.nickName=nickName;
        this.session=session;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public void setSession(WebSocketSession session) {
        this.session = session;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
