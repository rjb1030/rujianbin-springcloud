package rujianbin.autoconfiguration.tomcat.customizer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rujianbin on 2018/1/19.
 */
@Configuration
@ConfigurationProperties(prefix = "tomcat.customizer")
public class TomcatServerProperties {

    private RjbTomcatServer server;
    private RjbTomcatConnect connect;
    private RjbTomcatLog log;

    public RjbTomcatServer getServer() {
        return server;
    }

    public void setServer(RjbTomcatServer server) {
        this.server = server;
    }

    public RjbTomcatConnect getConnect() {
        return connect;
    }

    public void setConnect(RjbTomcatConnect connect) {
        this.connect = connect;
    }

    public RjbTomcatLog getLog() {
        return log;
    }

    public void setLog(RjbTomcatLog log) {
        this.log = log;
    }

    @Configuration
    @ConfigurationProperties(prefix = "tomcat.customizer.connect")
    public static class RjbTomcatConnect{
        private Integer maxConnections;
        private Integer maxThreads;
        private Integer connectionTimeout;

        public Integer getMaxConnections() {
            return maxConnections;
        }

        public void setMaxConnections(Integer maxConnections) {
            this.maxConnections = maxConnections;
        }

        public Integer getMaxThreads() {
            return maxThreads;
        }

        public void setMaxThreads(Integer maxThreads) {
            this.maxThreads = maxThreads;
        }

        public Integer getConnectionTimeout() {
            return connectionTimeout;
        }

        public void setConnectionTimeout(Integer connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
        }
    }

    @Configuration
    @ConfigurationProperties(prefix = "tomcat.customizer.server")
    public static class RjbTomcatServer{
        private Integer port;
        private String address;
        private String contextPath;
        private Integer sessionTimeout;
        private String tomcatBaseDir;

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContextPath() {
            return contextPath;
        }

        public void setContextPath(String contextPath) {
            this.contextPath = contextPath;
        }

        public Integer getSessionTimeout() {
            return sessionTimeout;
        }

        public void setSessionTimeout(Integer sessionTimeout) {
            this.sessionTimeout = sessionTimeout;
        }

        public String getTomcatBaseDir() {
            return tomcatBaseDir;
        }

        public void setTomcatBaseDir(String tomcatBaseDir) {
            this.tomcatBaseDir = tomcatBaseDir;
        }
    }

    @Configuration
    @ConfigurationProperties(prefix = "tomcat.customizer.log")
    public static class RjbTomcatLog{
        private String directory;
        private boolean enabled;
        private String prefix;
        private String suffix;

        public String getDirectory() {
            return directory;
        }

        public void setDirectory(String directory) {
            this.directory = directory;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getSuffix() {
            return suffix;
        }

        public void setSuffix(String suffix) {
            this.suffix = suffix;
        }
    }
}
