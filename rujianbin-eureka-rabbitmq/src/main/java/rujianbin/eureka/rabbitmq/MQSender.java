package rujianbin.eureka.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rujianbin.eureka.rabbitmq.config.QueueNames;

/**
 * Created by rujianbin on 2018/2/24.
 */

@Component
public class MQSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(String message){
        String msg = message+"_"+System.currentTimeMillis();
        this.amqpTemplate.convertAndSend(QueueNames.HELLO,msg);
    }
}
