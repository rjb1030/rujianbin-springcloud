package rujianbin.autoconfiguration.tomcat.customizer.config2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.stereotype.Component;
import rujianbin.autoconfiguration.tomcat.customizer.RjbAccessLogValveCustomizer;
import rujianbin.autoconfiguration.tomcat.customizer.RjbTomcatConnectorCustomizer;
import rujianbin.autoconfiguration.tomcat.customizer.TomcatServerProperties;

import java.io.File;

/**
 * Created by rujianbin on 2018/1/19.
 *
 * 容器生成后可以再改
 */
@Component
public class RjbEmbeddedServletContainerCustomizer implements EmbeddedServletContainerCustomizer {

    private final Logger logger = LoggerFactory.getLogger(RjbEmbeddedServletContainerCustomizer.class);

    @Autowired
    private TomcatServerProperties tomcatServerProperties;

    @Autowired
    private RjbAccessLogValveCustomizer rjbAccessLogValveCustomizer;

    @Autowired
    private RjbTomcatConnectorCustomizer rjbTomcatConnectorCustomizer;

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        //org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory
        //说明默认是的Tomcat容器
        logger.info("加载容器  "+container.getClass().getName());
        if(container instanceof TomcatEmbeddedServletContainerFactory){
            TomcatEmbeddedServletContainerFactory tomcatFactory = (TomcatEmbeddedServletContainerFactory) container;
            logger.info("自定义tomcat  port 原始值={}，新值={}",tomcatFactory.getPort(),tomcatServerProperties.getServer().getPort());
            tomcatFactory.setPort(tomcatServerProperties.getServer().getPort());
            logger.info("自定义tomcat  contextPath 原始值={}，新值={}",tomcatFactory.getContextPath(),tomcatServerProperties.getServer().getContextPath());
            tomcatFactory.setContextPath(tomcatServerProperties.getServer().getContextPath());
            logger.info("自定义tomcat  SessionTimeout 原始值={}，新值={}",tomcatFactory.getSessionTimeout(),tomcatServerProperties.getServer().getSessionTimeout());
            tomcatFactory.setSessionTimeout(tomcatServerProperties.getServer().getSessionTimeout());
            //设置Tomcat的根目录
            logger.info("自定义tomcat  BaseDirectory 新值={}",tomcatServerProperties.getServer().getTomcatBaseDir());
            tomcatFactory.setBaseDirectory(new File(tomcatServerProperties.getServer().getTomcatBaseDir()));
            //设置访问日志存放目录
            logger.info("自定义tomcat  日志 dir={},enable={},prefix={},suffix={}",rjbAccessLogValveCustomizer.getLogAccessLogValue().getDirectory(),rjbAccessLogValveCustomizer.getLogAccessLogValue().getEnabled(),rjbAccessLogValveCustomizer.getLogAccessLogValue().getPrefix(),rjbAccessLogValveCustomizer.getLogAccessLogValue().getSuffix());
            tomcatFactory.addContextValves(rjbAccessLogValveCustomizer.getLogAccessLogValue());
            //设置Tomcat线程数和连接数
            logger.info("自定义tomcat  连接数 MaxConnections={}，MaxThreads={}，ConnectionTimeout={}",tomcatServerProperties.getConnect().getMaxConnections(),tomcatServerProperties.getConnect().getMaxThreads(),tomcatServerProperties.getConnect().getConnectionTimeout());
            tomcatFactory.addConnectorCustomizers(rjbTomcatConnectorCustomizer);
            //初始化servletContext对象
            tomcatFactory.addInitializers((servletContext) -> {
                logger.info(" = = = = tomcat服务器信息 = = " + servletContext.getServerInfo());
            });
        }


    }


}
