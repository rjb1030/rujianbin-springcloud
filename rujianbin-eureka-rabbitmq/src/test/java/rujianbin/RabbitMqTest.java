package rujianbin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import rujianbin.eureka.rabbitmq.MQSender;

/**
 * Created by rujianbin on 2018/2/24.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EurekaRabbitMqApplication.class)
public class RabbitMqTest {

    @Autowired
    private MQSender sender;

    @Test
    public void sendMQ(){
        for(int i=1;i<=5;i++){
            sender.send("哈哈");
        }
    }
}
