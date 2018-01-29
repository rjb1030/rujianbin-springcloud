package rujianbin.app.websocket.chatroom.jdknio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rujianbin on 2018/1/29.
 */
public class WebSocketHandShake {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketHandShake.class);

    private static final String guid = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    private static final String keyName = "Sec-WebSocket-Key";

    /**
     * 握手响应
     * @param message
     * @return
     */
    public static String websocketHandShake(String message){
        try {
            if(message.contains(keyName)){
                String key = getSecWebSocketKey(message);
                return "HTTP/1.1 101 Switching Protocols" +
                        "\r\nUpgrade: websocket"+
                        "\r\nConnection: Upgrade" +
                        "\r\nSec-WebSocket-Accept: "+
                        getSecWebSocketAccept(key) + "\r\n\r\n";
            }else{
                return null;
            }
        } catch (Exception e) {
            logger.error("获取websocket握手协议响应 失败",e);
            return null;
        }
    }

    /**
     * 加密key
     * @param key
     * @return
     * @throws Exception
     */
    private static String getSecWebSocketAccept(String key) throws Exception{
        key += guid;
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(key.getBytes("ISO-8859-1") , 0 , key.length());
        byte[] sha1Hash = md.digest();
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(sha1Hash);
    }

    /**
     * 获取WebSocket请求的SecKey
     * @param message
     * @return
     */
    private static String getSecWebSocketKey(String message){
        //构建正则表达式，获取Sec-WebSocket-Key: 后面的内容
        Pattern p = Pattern.compile("^(Sec-WebSocket-Key:).+",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher m = p.matcher(message);
        if (m.find())
        {
            // 提取Sec-WebSocket-Key
            String foundstring = m.group();
            return foundstring.split(":")[1].trim();
        }
        else
        {
            return null;
        }
    }
}
