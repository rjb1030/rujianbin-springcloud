//package rujianbin.autoconfiguration.tomcat.customizer.config1;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
//import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import rujianbin.autoconfiguration.tomcat.customizer.RjbAccessLogValveCustomizer;
//import rujianbin.autoconfiguration.tomcat.customizer.RjbTomcatConnectorCustomizer;
//import rujianbin.autoconfiguration.tomcat.customizer.TomcatServerProperties;
//
//import java.io.File;
//
///**
// * Created by rujianbin on 2018/1/19.
// * 在创建tomcat容器时就定义
// */
//
//
//
//@Configuration
//public class RjbTomcatConfiguration {
//
//    @Autowired
//    private TomcatServerProperties tomcatServerProperties;
//
//    @Autowired
//    private RjbAccessLogValveCustomizer rjbAccessLogValveCustomizer;
//
//    @Autowired
//    private RjbTomcatConnectorCustomizer rjbTomcatConnectorCustomizer;
//
//    @Bean
//    public EmbeddedServletContainerFactory createEmbeddedServletContainerFactory()
//    {
//        TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();
//        tomcatFactory.setPort(tomcatServerProperties.getServer().getPort());
//        tomcatFactory.setContextPath(tomcatServerProperties.getServer().getContextPath());
//        tomcatFactory.setSessionTimeout(tomcatServerProperties.getServer().getSessionTimeout());
//        //设置Tomcat的根目录
//        tomcatFactory.setBaseDirectory(new File(tomcatServerProperties.getServer().getTomcatBaseDir()));
//        //设置访问日志存放目录
//        tomcatFactory.addContextValves(rjbAccessLogValveCustomizer.getLogAccessLogValue());
//        //设置Tomcat线程数和连接数
//        tomcatFactory.addConnectorCustomizers(rjbTomcatConnectorCustomizer);
//        return tomcatFactory;
//    }
//
//}
