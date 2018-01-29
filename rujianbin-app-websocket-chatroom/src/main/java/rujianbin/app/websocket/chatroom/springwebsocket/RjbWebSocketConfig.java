package rujianbin.app.websocket.chatroom.springwebsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Created by rujianbin on 2018/1/18.
 * 基于spring web 访问socket端点
 * ws://{domain}:{port}/{context}/{endpoint}
 */

@EnableWebSocket
@EnableWebMvc
@Configuration
public class RjbWebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer{

    private final Logger logger = LoggerFactory.getLogger(RjbWebSocketConfig.class);

    @Bean
    public ServerEndpointExporter serverEndpointExporter(ApplicationContext context) {
        return new ServerEndpointExporter();
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        logger.info("WebSocketHandlerRegistry注册");

        //WebIM WebSocket通道
        registry.addHandler(new RjbTextWebSocketHandler(),"/websocket")
                //允许连接的域,只能以http或https开头
                .setAllowedOrigins(new String[]{"*"})
                .addInterceptors(new RjbHttpSessionHandshakeInterceptor());
//                .withSockJS();   //如果前端是websocketJs通信则加上
    }

}
