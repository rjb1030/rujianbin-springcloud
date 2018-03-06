package rujianbin.eureka.rabbitmq.stream.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Created by rujianbin on 2018/3/5.
 * 定义outputchannle  默认接口Source.class
 * 通道名称OrderCreateOutput  类型是@Output
 *
 */
public interface SendChannel {

//    String OrderCreate_OUTPUT = "orderCreateOutput";

    /**
     * 向input通道写消息
     * @return
     */
    @Output(ReceiveChannel.OrderCreate_INPUT)
    MessageChannel orderCreateOutput();
}
