package rujianbin.app.websocket.chatroom.jdknio;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rujianbin on 2018/1/29.
 * websocekt通信的格式
 * 常用16进制
 * 0xff = 1111 1111
 * 0x7f = 0111 1111
 * 0xf  = 0000 1111
 * 0xfe = 1111 1110
 */
public class RjbSocketMessage {

    public enum Opcode{
        Continuation(0,"继续"),
        TextFrame(1,"文本"),
        BinaryFrame(2,"二进制文件"),
        ConnectionClose(8,"连接关闭"),
        Ping(9,"PING"),
        Pong(10,"PONG");

        private Opcode(int typeCode,String typeName){
            this.typeCode = typeCode;
            this.typeName = typeName;
        }

        int typeCode;
        String typeName;

        public int getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(int typeCode) {
            this.typeCode = typeCode;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    /**
     *  第1个字节（1 byte = 8 bit）
     *      bit(1) FIN:是否值最后一帧，即结束标记。 1结束  0未结束，还有其他数据在上传
     *      bit(2,4) 预留 默认0
     *      bit(5,8) Opcode:消息类型  0:Continuation 1:TextFrame 2:BinaryFrame 8:Connection Close Frame 9:Ping Frame 10:Pong Frame
     */
    private byte byte1;
    /**
     *  第2个字节
     *      bit(1) MASK:是否有掩码  1是  0不是
     *      bit(2,8) 消息长度 当这个7 bit的数据 <=125时表示实际的消息长度。当它=126 时后面的2 个字节也是表示数据长度，当它 == 127 时后面的8个字节也表示数据长度
     */
    private byte byte2;

    private boolean hasExtendDataByteLen=false;

    /**
     * extends消息长度
     */
    private byte[] extendDataByteLen;

    private boolean hasMaskingKey=false;
    /**
     * 掩码实体
     */
    private byte[] maskingKey;

    private byte[] data;

    private String message;

    public RjbSocketMessage(){

    }

    public static void main(String[] args) {
        Integer num =16333;

        System.out.println(Integer.toBinaryString(num));
        System.out.println(Integer.toBinaryString(num & 0xff));
    }

    public RjbSocketMessage buildByte1(boolean isEof,Opcode opcode){
        if(isEof){
            this.byte1 = (byte)(1<<7 | (opcode.getTypeCode() & 0xff));
        }else{
            this.byte1 = (byte)(opcode.getTypeCode() & 0xff);
        }
        return this;
    }

    public RjbSocketMessage buildByte2(boolean isMask,String message){
        if(!StringUtils.isEmpty(message)){
            buildMessage(message);
            /**
             * 掩码标记先处理好
             */
            if(isMask){
                this.byte2 =  (byte)(1<<7 & 0xff);
            }
            byte[] msgBytes = message.getBytes();
            int length = msgBytes.length;
            /**
             * 小于等于125个字节，length直接转二进制
             */
            if(length<=125){
                this.byte2 = (byte)(this.byte2 | (length & 0xff));
            }else{
                //如果长度<=65535即2个字节可以表示的长度 则扩展2个字节表示长度，byte2中写入126
                if(length<=((1<<16)-1)){
                    this.byte2 = (byte)(this.byte2 | (126 & 0xff));
                    extendDataByteLen = buildExtendDataByteLen(length);
                }
                //如果长度在8个字节可以表示的长度 则扩展8个字节表示长度，byte2中写入127
                else{
                    this.byte2 = (byte)(this.byte2 | (127 & 0xff));
                    extendDataByteLen = buildExtendDataByteLen(length);
                }
            }

        }

        return this;
    }

    /**
     * 当内容的字节数大于125时，扩展表示内容长度的新字节
     * @param length
     * @return
     */
    private byte[] buildExtendDataByteLen(int length){

        byte[] result;
        if(length<=((1<<16)-1)){
            result = new byte[2];
            result[0] = (byte)(length & 0xff);
            result[1] = (byte)(length>>8 & 0xff);
            hasExtendDataByteLen = true;
            return result;
        }
        else if(length<=((1<<32)-1)){
            result = new byte[8];
            result[0] = (byte)(length & 0xff);
            result[1] = (byte)(length>>8 & 0xff);
            result[2] = (byte)(length>>16 & 0xff);
            result[3] = (byte)(length>>32 & 0xff);
            result[4] = 0;
            result[5] = 0;
            result[6] = 0;
            result[7] = 0;
            hasExtendDataByteLen = true;
            return result;
        }else{
            throw new RuntimeException("内容太长，不能发送");
        }
    }


    private void buildMessage(String message){
        this.message = message;
        this.data = message.getBytes();
    }

    public byte[] toBytes(){
        List<Byte> byteList = new ArrayList<>();
        byteList.add(byte1);
        byteList.add(byte2);
        if(hasExtendDataByteLen){
            for(byte tmp : extendDataByteLen){
                byteList.add(tmp);
            }
        }
        if(hasMaskingKey){
            for(byte tmp : maskingKey){
                byteList.add(tmp);
            }
        }
        for(byte tmp : data){
            byteList.add(tmp);
        }
        return  ArrayUtils.toPrimitive((Byte[])byteList.toArray(new Byte[byteList.size()]));
    }
}
