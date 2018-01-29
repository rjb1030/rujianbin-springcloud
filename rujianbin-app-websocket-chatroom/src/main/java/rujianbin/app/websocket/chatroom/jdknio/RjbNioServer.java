package rujianbin.app.websocket.chatroom.jdknio;

import com.fasterxml.jackson.databind.util.ArrayBuilders;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rujianbin.common.utils.RjbStringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rujianbin on 2018/1/23.
 * http://old.zhanghuanglong.com/im/chat/  聊天室样例
 *
 *
 * accpt连接时会传输的内容
 *   GET / HTTP/1.1
     Host: localhost:7090
     Connection: Upgrade
     Pragma: no-cache
     Cache-Control: no-cache
     Upgrade: websocket
     Origin: http://ecom-qa.baiwandian.cn
     Sec-WebSocket-Version: 13
     User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.84 Safari/537.36
     Accept-Encoding: gzip, deflate, br
     Accept-Language: zh-CN,zh;q=0.9
     Sec-WebSocket-Key: U4yQYBpKsiASe0nbPvTK8g==
     Sec-WebSocket-Extensions: permessage-deflate; client_max_window_bits
 */
public class RjbNioServer {

    private static SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    private AtomicInteger onlineUserCount = new AtomicInteger(0);
    private Map<SocketChannel,RjbClientSocket> socketClientMap = new ConcurrentHashMap<>();

    private final Logger logger = LoggerFactory.getLogger(RjbNioServer.class);

    public RjbNioServer(){}

    /**
     * socket 绑定地址  IP:PORT
     */
    private InetSocketAddress address;

    /**
     * 服务端套接字通道
     */
    private ServerSocketChannel serverSocketChannel;
    /**
     * 服务端套接字
     */
    private ServerSocket serverSocket;

    /**
     * 选择器
     */
    private Selector selector;
    /**
     * 缓冲区大小
     */
    private  int BLOCK = 1024;

    /**
     *
     * @param selector  注册在哪个selector
     * @param address   监听地址
     */
    public RjbNioServer createServerSocket(Selector selector,InetSocketAddress address) throws IOException{
        this.selector=selector;
        this.address=address;

        /**
         * 打开服务器套接字通道
         */
        serverSocketChannel = ServerSocketChannel.open();
        /**
         * 服务器配置为非阻塞。socketChannel有非阻塞模式，fileChannel则没有非阻塞模式
         * 与Selector一起使用，必须处于非阻塞模式。
         */
        serverSocketChannel.configureBlocking(false);
        /**
         * 获取套接字socket
         */
        serverSocket = serverSocketChannel.socket();

        /**
         * 绑定IP和端口
         */
        serverSocket.bind(address);

        /**
         * 通道注册到selector上以及感兴趣事件（初次一般是ACCEPT事件）
         * SelectionKey和selector，channel的关系：
         * 一个selector上可以注册多个channel(即1个selector可以管理多个通道)
         * 一个channel注册在一个selector上  产生一个SelectionKey（selector+channel 构建一个SelectionKey），channel的keys持有该引用。该SelectionKey里可以有多个就绪事件
         * SelectionKey有selector和channel的引用
         * selector有所有注册在上面的SelectionKey集合
         *
         * 注意，注册时可以同时有多个感兴趣事件。注册时可以用“位或”操作，同时关心多个事件。SelectionKey.OP_READ | SelectionKey.OP_WRITE
         */
        SelectionKey selectionKey = serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        return this;
    }

    public void listening()throws IOException{
        while (true) {
            try {
                /**
                 * selector上监听感兴趣事件，如果无感兴趣事件，此处会阻塞。阻塞通过代表感兴趣事件已经产生
                 */
                this.selector.select();
                /**
                 * 返回此选择器的已产生的感兴趣事件。
                 * 返回多个selectionKeys 表示该selector注册了多个通道
                 * 如果一个通道监听了多个感兴趣事件，则返回的还是一个SelectionKey，只是该SelectionKey有多个状态位isAcceptable()，isReadable()，isWritable()
                 */
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                /**
                 * 遍历感兴趣事件，即SelectionKey
                 */
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    //注意，一个SelectionKey可以同时又多个感兴趣事件。这取决于注册时，关注了几个事件。注册时可以用“位或”操作，同时关心多个事件。SelectionKey.OP_READ | SelectionKey.OP_WRITE
                    //如果是多个感兴趣事件，则不能if else if了
                    if(selectionKey.isAcceptable()){
                        handleAccpet(selectionKey);
                    }else if(selectionKey.isValid() && selectionKey.isReadable()){
                        handleRead(selectionKey);
                    }else if(selectionKey.isValid() && selectionKey.isWritable()){
                        handleWrite(selectionKey);
                    }
                }
            } catch (IOException e) {
                logger.error("处理感兴趣事件异常",e);
            }
        }
    }

    private void handleAccpet(SelectionKey selectionKey)throws IOException{
        try {
            // 返回为之创建此键的通道。
            ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();

            // 接受到此通道套接字的连接。
            // 此方法返回的套接字通道（如果有）将处于阻塞模式。
            SocketChannel socket = server.accept();
            int count = onlineUserCount.incrementAndGet();
            socketClientMap.put(socket,new RjbClientSocket(socket,null,null));
            logger.info("有新连接 "+socket.toString()+"   port:"+address.getPort()+" 当前客户端连接数="+count);
            // 配置为非阻塞
            socket.configureBlocking(false);
            // 注册到selector，等待连接
            socket.register(selector, SelectionKey.OP_READ,"http_first_handshake");

        } catch (IOException e) {
            logger.error("处理accept事件异常",e);
        }
    }




    private void handleRead(SelectionKey selectionKey)throws IOException{
        try {

            // 返回为之创建此键的通道。
            SocketChannel socket = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(BLOCK);
            //读取服务器发送来的数据到缓冲区中
            int count = 0;

            StringBuilder builder = new StringBuilder();
            try {
                WebSocketDataParse parse = new WebSocketDataParse(selectionKey.attachment()!=null);
                while ((count=socket.read(buffer))>0) {
                    buffer.flip();
                    byte[] b = new byte[buffer.limit()];
                    buffer.get(b);
                    parse.add(b);
                    buffer.clear();
                }
                builder.append(parse.parse());
            } catch (IOException e) {
                logger.error("连接断开",e);
                socket.close();
                selectionKey.cancel();
                onlineUserCount.decrementAndGet();
                socketClientMap.remove(socket);
            }

            System.out.println("=======================   客户端数据   =========================================");
            System.out.println(builder.toString());
            System.out.println("=======================   客户端数据 end   =========================================");

            //响应握手
            String response = WebSocketHandShake.websocketHandShake(builder.toString());
            if(!StringUtils.isEmpty(response)){
                //握手消息：直接响应
                socket.write(ByteBuffer.wrap(response.getBytes()));
                selectionKey.attach(null);

                String nickName = WebSocketHandShake.findUrlParamByRegx("nickName",builder.toString());
                String userName = WebSocketHandShake.findUrlParamByRegx("userName",builder.toString());
                socketClientMap.get(socket).setNickName(nickName);
                socketClientMap.get(socket).setUserName(userName);
                //加入聊天室消息，广播
                broadcast(socket,"系统",nickName+"加入聊天室");
            }else{
                //普通消息，广播
                broadcast(socket,socketClientMap.get(socket).getNickName(),builder.toString());
            }

        } catch (Exception e) {
            logger.error("处理read事件异常",e);
        }
    }

    private void handleWrite(SelectionKey selectionKey)throws IOException{
        try {
            logger.info("有写事件");
            // 返回为之创建此键的通道。
            SocketChannel socket = (SocketChannel) selectionKey.channel();
            String sendText="hello  i am server with port " + address.getPort()+" @"+fm.format(new Date());
            //向缓冲区中输入数据
            ByteBuffer buffer = ByteBuffer.allocate(BLOCK);
            buffer.put(sendText.getBytes());
            //将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
            buffer.flip();
            //输出到通道
            socket.write(buffer);
            socket.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            logger.error("处理write事件异常",e);
        }
    }

    public void broadcast(SocketChannel self,String from,String msg) throws IOException{
        if(StringUtils.isEmpty(msg)){
            return;
        }
        for(Map.Entry<SocketChannel,RjbClientSocket> entry : socketClientMap.entrySet()){
            SocketChannel socket = entry.getKey();
            if(self != socket){
                writeMsg(socket,from,msg);
            }

        }
    }


    public void writeMsg(SocketChannel socket,String from,String msg)throws IOException{
        if(StringUtils.isEmpty(msg)){
            return;
        }
        RjbMessage obj = new RjbMessage();
        obj.setFrom(from);
        obj.setContent(msg);
        obj.setOnlineCount(onlineUserCount.get());
        String message = RjbStringUtils.ObjectToString(obj);
        byte[] msgBytes = new RjbSocketMessage()
                .buildByte1(true,RjbSocketMessage.Opcode.TextFrame)
                .buildByte2(false,message)
                .toBytes();
        ByteBuffer buffer = ByteBuffer.allocate(msgBytes.length);
        buffer.put(msgBytes);
        buffer.flip();
        socket.write(buffer);
    }
}
