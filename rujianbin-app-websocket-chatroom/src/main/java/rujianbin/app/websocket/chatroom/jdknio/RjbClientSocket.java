package rujianbin.app.websocket.chatroom.jdknio;

import java.nio.channels.SocketChannel;

/**
 * Created by rujianbin on 2018/1/29.
 */
public class RjbClientSocket {


    private SocketChannel socketChannel;

    private String nickName;

    private String userName;

    public RjbClientSocket(SocketChannel socketChannel,String nickName,String userName){
        this.socketChannel = socketChannel;
        this.nickName = nickName;
        this.userName = userName;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
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
