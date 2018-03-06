package rujianbin.eureka.rabbitmq.bus;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import rujianbin.eureka.rabbitmq.config.QueueNames;

/**
 * Created by rujianbin on 2018/2/24.
 */

@Component
@RabbitListener(queues = QueueNames.HELLO)
public class MQReceiver {

    @RabbitHandler
    public void process(String message){
        System.out.println("接收信息---->"+message);
    }

}
