package rujianbin.app.websocket.chatroom.jdknio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by rujianbin on 2018/1/24.
 */

@Component
public class StartUpConfiguration{

    private final Logger logger = LoggerFactory.getLogger(StartUpConfiguration.class);

    private AtomicInteger count = new AtomicInteger(0);

    @Value("${nio.jdk.socket-port}")
    private Integer socketPort;

    @Value("${nio.jdk.socket-num}")
    private int socketNum;


    @PostConstruct
    public void startup() throws Exception {
        for(int i=1;i<=socketNum;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int port = socketPort+count.get();
                    count.incrementAndGet();
                    try {
                        logger.info("RjbNioServer启动 port="+port);
                        new RjbNioServer().createServerSocket(Selector.open(),new InetSocketAddress("localhost",port))
                                .listening();
                    } catch (IOException e) {
                        logger.info("RjbNioServer启动失败 port="+port,e);
                    }
                }
            }).start();
        }
    }

}
