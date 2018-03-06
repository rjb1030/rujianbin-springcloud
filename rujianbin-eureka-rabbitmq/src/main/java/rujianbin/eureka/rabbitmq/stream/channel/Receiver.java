package rujianbin.eureka.rabbitmq.stream.channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

/**
 * Created by rujianbin on 2018/3/5.
 */

@EnableBinding(value={ReceiveChannel.class,SendChannel.class})
public class Receiver {

    private static Logger logger  = LoggerFactory.getLogger(Receiver.class);

    /**
     * 箭筒input通道 消费消息
     * @param payload
     */
    @StreamListener(ReceiveChannel.OrderCreate_INPUT)
    public void receive(Object payload){
        logger.info("Receiver----->"+payload);
    }
}
