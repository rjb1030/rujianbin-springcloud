package rujianbin.eureka.rabbitmq.stream.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by rujianbin on 2018/3/5.
 * 定义InputChannle  默认接口Sink.class
 * 通道名称orderCreateInput  类型@Input
 *
 */
public interface ReceiveChannel {

    String OrderCreate_INPUT = "orderCreateInput";

    @Input(ReceiveChannel.OrderCreate_INPUT)
    SubscribableChannel orderCreateInput();
}
