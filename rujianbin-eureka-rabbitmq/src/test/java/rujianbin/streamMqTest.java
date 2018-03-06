package rujianbin;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by rujianbin on 2018/3/5.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = EurekaRabbitMqApplication.class)
@WebAppConfiguration
public class streamMqTest {

    @Autowired
    @Qualifier("orderCreateInput")
    private MessageChannel output;

    @Test
    public void send(){
        output.send(MessageBuilder.withPayload("message from hello world").build());
    }

}
