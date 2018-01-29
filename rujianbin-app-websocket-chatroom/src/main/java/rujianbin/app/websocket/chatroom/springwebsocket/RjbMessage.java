package rujianbin.app.websocket.chatroom.springwebsocket;

import java.io.Serializable;

/**
 * Created by rujianbin on 2018/1/18.
 */
public class RjbMessage implements Serializable {

    private static final long serialVersionUID = -1626424369503425841L;
    private String from;
    private String content;
    private Integer onlineCount;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(Integer onlineCount) {
        this.onlineCount = onlineCount;
    }
}
