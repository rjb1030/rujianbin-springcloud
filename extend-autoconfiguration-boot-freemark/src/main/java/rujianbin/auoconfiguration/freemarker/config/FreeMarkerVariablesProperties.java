package rujianbin.auoconfiguration.freemarker.config;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rujianbin on 2017/11/24.
 *
 *
 * springboot 1.5以后不支持ConfigurationProperties的locations
 * 故此处直接改用兼容的加载模式@PropertySource默认解析properties文件 可以自定义factory(YamlPropertySourceFactory)使能解析yml文件
 *
 */
@Configuration
@ConfigurationProperties(prefix = "spring.freemarker")
public class FreeMarkerVariablesProperties {

    Map<String, Object> variables = new HashMap();

    public Map<String, Object> getVariables() {
        return this.variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}
