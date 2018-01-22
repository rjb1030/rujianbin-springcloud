package rujianbin.autoconfiguration.tomcat.customizer;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.stereotype.Component;
import rujianbin.autoconfiguration.tomcat.customizer.config2.RjbEmbeddedServletContainerCustomizer;

/**
 * Created by rujianbin on 2018/1/19.
 */

@Component
public class RjbTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

    private final Logger logger = LoggerFactory.getLogger(RjbTomcatConnectorCustomizer.class);

    @Autowired
    private TomcatServerProperties tomcatServerProperties;

    public void customize(Connector connector)
    {
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        //设置最大连接数
        logger.info("自定义tomcat MaxConnections 原始值={}，新值={}",protocol.getMaxConnections(),tomcatServerProperties.getConnect().getMaxConnections());
        protocol.setMaxConnections(tomcatServerProperties.getConnect().getMaxConnections());
        //设置最大线程数
        logger.info("自定义tomcat MaxThreads 原始值={}，新值={}",protocol.getMaxThreads(),tomcatServerProperties.getConnect().getMaxThreads());
        protocol.setMaxThreads(tomcatServerProperties.getConnect().getMaxThreads());
        logger.info("自定义tomcat ConnectionTimeout 原始值={}，新值={}",protocol.getConnectionTimeout(),tomcatServerProperties.getConnect().getConnectionTimeout());
        protocol.setConnectionTimeout(tomcatServerProperties.getConnect().getConnectionTimeout());
        /**
         * AcceptCount指定当所有可以使用的处理请求的线程数都被使用时，可以放到处理队列中的请求数，超过这个数的请求将不予处理
         */
        logger.info("自定义tomcat AcceptCount 原始值={}，新值={}",protocol.getAcceptCount(),protocol.getAcceptCount());
        logger.info("自定义tomcat AcceptorThreadCount 原始值={}，新值={}",protocol.getAcceptorThreadCount(),protocol.getAcceptorThreadCount());
    }
}
