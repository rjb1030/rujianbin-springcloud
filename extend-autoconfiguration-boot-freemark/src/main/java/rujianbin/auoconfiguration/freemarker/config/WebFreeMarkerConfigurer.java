package rujianbin.auoconfiguration.freemarker.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration.FreeMarkerWebConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.Servlet;

/**
 * 定义一些freemarker的全局变量
 * @author rujianbin
 *
 * spring:
 *   freemarker:
       variables:
         static: http://127.0.0.1/static/      自定义变量：静态文件前缀路径
         jsVersion: 1.0.0                      自定义变量：后缀版本号
       cache: false
       charset: UTF-8
       template-loader-path: classpath:/template/freemarker/     模板文件加载路径
       settings:
         template_update_delay: 0
 *
 */

@Configuration
@ConditionalOnClass({Servlet.class})
@ConditionalOnWebApplication
@EnableConfigurationProperties(FreeMarkerVariablesProperties.class)
public class WebFreeMarkerConfigurer extends FreeMarkerWebConfiguration{

	@Bean
	@ConditionalOnMissingBean({FreeMarkerConfig.class})
	public FreeMarkerConfigurer freeMarkerConfigurer(@Autowired FreeMarkerVariablesProperties freeMarkerVariables) {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setFreemarkerVariables(freeMarkerVariables.getVariables());
		applyProperties(configurer);
		return configurer;
	}

}
