package rujianbin.app.websocket.chatroom.jdknio;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rujianbin on 2018/1/29.
 * websocket 数据遵循一定协议 http://www.cnblogs.com/smark/archive/2012/11/26/2789812.html
 * 第1个字节（1 byte = 8 bit）
 *      bit(1) FIN:是否值最后一帧，即结束标记。 1结束  0未结束，还有其他数据在上传
 *      bit(2,4) 预留 默认0
 *      bit(5,8) Opcode:消息类型  0:Continuation 1:TextFrame 2:BinaryFrame 8:Connection Close Frame 9:Ping Frame 10:Pong Frame
 * 第2字节
 *      bit(1) MASK:是否有掩码  1是  0不是
 *      bit(2,8) 消息长度 当这个7 bit的数据 <=125时表示实际的消息长度。当它=126 时后面的2 个字节也是表示数据长度，当它 == 127 时后面的8个字节也表示数据长度
 *
 * 掩码实体字节： 如果掩码标记为1  则有4个字节存储掩码实体。消息内容要和掩码实体异或后才可以
 *
 *
 *
 */
public class WebSocketDataParse {

    private final Logger logger = LoggerFactory.getLogger(WebSocketDataParse.class);

    private boolean isHttpFirstHandShake=false;

    private List<Byte> messageByteList = new ArrayList<>();

    public WebSocketDataParse(boolean isHttpFirstHandShake){
        this.isHttpFirstHandShake = isHttpFirstHandShake;
    }

    public void add(byte[] message){
        for(int i=0;i<message.length;i++){
            messageByteList.add(Byte.valueOf(message[i]));
        }
    }

    public String parse()throws Exception{
        if(messageByteList.size()<1){
            return "";
        }
        byte[] message = ArrayUtils.toPrimitive((Byte[])messageByteList.toArray(new Byte[messageByteList.size()]));
        /**
         * 握手的消息是基于http的 不遵循websocket协议。故解析所有的字节
         */
        if(isHttpFirstHandShake){
            return Charset.forName("UTF-8").newDecoder().decode(ByteBuffer.wrap(message)).toString();
        }
        int dataByteStart = 2;
        byte byte1 = messageByteList.get(0);
        byte byte2 = messageByteList.get(1);
        /**
         * 是否最后一帧
         * byte1=-127 解析为最高位是1
         * 说明：
         *      带符号二进制，最高位1表示负数，0表示正数
         *      >>表示带符号右移，最高位都是用符号位补充的
         *      >>>无符号位右移，最高位都是0补充
         */
        boolean isFin = (byte1>>7)<0;
        /**
         * 消息类型
         */
        int Opcode = byte1 & 0xF;
        /**
         * 是否有掩码
         */
        boolean isMask = (byte2>>7)<0;

        /**
         * 数据长度
         */
        int dataLen = byte2 & 0x7F;  //0x7F = 0111 1111

        /**
         * 处理消息长度
         */
        if(dataLen<=125){

        }else if(dataLen<=126){
            dataByteStart+=2;
        }else if(dataLen<=127){
            dataByteStart+=8;
        }

        byte[] maskingKey=new byte[4];
        if(isMask){
            maskingKey = ArrayUtils.subarray(message,dataByteStart,dataByteStart+4);
            dataByteStart+=4;
        }
        byte[] playloadData = ArrayUtils.subarray(message,dataByteStart,message.length);
        byte[] parsePlayloadData = new byte[playloadData.length];
        if(isMask){
            //如果是有掩码，则数据数组和掩码实体要异或运算
            for(int i = 0; i < playloadData.length ; i++){
                parsePlayloadData[i] = ((Integer)(playloadData[i] ^  maskingKey[i%4])).byteValue();
            }
        }else{
            for(int i = 0; i < playloadData.length ; i++){
                parsePlayloadData[i] = playloadData[i];
            }
        }
//        logger.info("接收数组({})={}",message.length,message);
//        logger.info("isFin={},Opcode={},isMask={},dataLen={}",isFin,Opcode,isMask,dataLen);
//        logger.info("maskingKey={},parsePlayloadData={}",maskingKey,parsePlayloadData);
        return Charset.forName("UTF-8").newDecoder().decode(ByteBuffer.wrap(parsePlayloadData)).toString();
    }


}
