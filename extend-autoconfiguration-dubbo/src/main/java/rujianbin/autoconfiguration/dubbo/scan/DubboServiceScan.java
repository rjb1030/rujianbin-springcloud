package rujianbin.autoconfiguration.dubbo.scan;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.spring.ServiceBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.Ordered;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import rujianbin.autoconfiguration.dubbo.annotation.DubboService;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Component
//@Configuration
//@EnableConfigurationProperties(DubboProperties.class)
public class DubboServiceScan implements BeanFactoryPostProcessor, ApplicationContextAware, Ordered{

	private ApplicationContext applicationContext;

	@Override
	public int getOrder() {
		return 200000;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Scanner scanner = new Scanner((BeanDefinitionRegistry)beanFactory);
	    scanner.setResourceLoader(this.applicationContext);
	    //@EnableAutoConfiguration注解的类所在的路径包
	    List<String> packages = AutoConfigurationPackages.get(this.applicationContext);
	    System.out.println("dubboServiceScan---->"+StringUtils.arrayToCommaDelimitedString(StringUtils.toStringArray(packages)));
	    scanner.scan(StringUtils.toStringArray(packages));
	   
	}
	
	class Scanner extends ClassPathBeanDefinitionScanner{
		
		private final Logger log = LoggerFactory.getLogger(Scanner.class);
		
		private BeanDefinitionRegistry registry;
		//key是实现类className  value=该实现类对应接口注解的版本号
		private Map<String, String> impl_interfaceVersion = new HashMap<String,String>();
		private Map<String, Class> impl_interface = new HashMap<String,Class>();
		
		public Scanner(BeanDefinitionRegistry registry){
	      super(registry);
	      this.registry = registry;
	    }
		
		//这个方法的实现，既可以自己去判断注解，也可以用通用的filter（父方法）
		protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
			//因我们要注册实现类到spring,故接口直接跳过。
			if (!metadataReader.getClassMetadata().isInterface()) {
				//获取实现类class继承的所有接口
				for (String face : metadataReader.getClassMetadata().getInterfaceNames()) {
					try {
						Class cz = Class.forName(face);
						StandardAnnotationMetadata metadata = new StandardAnnotationMetadata(cz);
						//因为我们注解时放在接口上的，故实现类的接口上有对应注解，则是候选类
						if (metadata.hasAnnotation(DubboService.class.getName())) {
							impl_interface.put(metadataReader.getClassMetadata().getClassName(), cz);
							//获取接口上的DubboService注解
							DubboService ann = (DubboService)cz.getAnnotation(DubboService.class);
							String version = ann.version();
							if(!StringUtils.isEmpty(version)){
								impl_interfaceVersion.put(metadataReader.getClassMetadata().getClassName(), version);
							}
							return true;
						}
					} catch (ClassNotFoundException e) {
						throw new BeanCreationException("", e);
					}
				}
			}
			return false;
		}
		
		//原父方法是必须要实现类或者静态内部内才能被注册，即返回true。而我们这里这层逻辑控制在isCandidateComponent(MetadataReader metadataReader)方法里了
		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			return true;
		}
		
		//basePackages要传入实现类的base包
		protected Set<BeanDefinitionHolder> doScan(String[] basePackages){
			Set beanDefinitions = new LinkedHashSet();
			for (String basePackage : basePackages) {
				//获取指定包下的所有class,由于isCandidateComponent方法，返回的是所有有DubboService注解接口的对应的所有实现类
				Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
				Properties props = new Properties();
				for (BeanDefinition candidate : candidates) {
					try{
						Class interfaceCz = impl_interface.get(candidate.getBeanClassName());

						String implClassName = StringUtils.uncapitalize(Class.forName(candidate.getBeanClassName()).getSimpleName());
						String dubboBeanName = implClassName + "DubboServiceImpl";

						String version = impl_interfaceVersion.get(candidate.getBeanClassName());
						if (StringUtils.isEmpty(version)) {
							//加载版本号文件
							if(props.getProperty("dubbo.service.version")==null){
								props = loadProperties(interfaceCz);
							}
							version = props.getProperty("dubbo.service.version");
						}

						if (StringUtils.isEmpty(version)) {
							throw new BeanCreationException(
									String.format("create %s bean error version can't be null", new Object[] { candidate.getBeanClassName() }));
						}

						Map appBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
								DubboServiceScan.this.applicationContext, ApplicationConfig.class, false, false);
						ApplicationConfig applicationConfig = (ApplicationConfig) ((Map.Entry) appBeans.entrySet()
								.iterator().next()).getValue();
						
						RootBeanDefinition beanDefinition = new RootBeanDefinition();
						beanDefinition.setBeanClass(ServiceBean.class);
						beanDefinition.setLazyInit(false);
						beanDefinition.getPropertyValues().addPropertyValue("application", applicationConfig);
						beanDefinition.getPropertyValues().addPropertyValue("id", dubboBeanName);
						beanDefinition.getPropertyValues().addPropertyValue("ref", new RuntimeBeanReference(implClassName));
						beanDefinition.getPropertyValues().addPropertyValue("interface", interfaceCz.getName());
						beanDefinition.getPropertyValues().addPropertyValue("version", version);

						this.log.info("【dubbo服务接口注册】 Init dubbo service {} version {} interface {}",
								new Object[] { dubboBeanName, version, candidate.getBeanClassName() });
						this.registry.registerBeanDefinition(dubboBeanName, beanDefinition);
					}catch(Exception e){
						throw new BeanCreationException(
								String.format("create %s bean error ", new Object[] { candidate.getBeanClassName() }),e);
					}
				}
			}

			return beanDefinitions;
		}
		
		private Properties loadProperties(Class cz) {
			String path = null;
			InputStream ips = null;
			Properties props = new Properties();
			try {
				if (props.isEmpty()) {
					path = cz.getResource("")
							.toString().replace(new StringBuilder().append("/")
									.append(cz.getPackage().getName().replace(".", "/")).append("/").toString(), "")
							+ "/api.version.properties";
					// 加载classpath下的api.version.properties文件
					ips = DubboServiceScan.this.applicationContext.getResource(path).getInputStream();
					this.log.info("loading api.version.properties form  path:{}", path);
					props.load(ips);

					if (null != ips){
						try {
							ips.close();
						} catch (IOException e) {
							this.log.warn("close stream error", e);
						}
					}
						
				}

			} catch (IOException e) {
				throw new BeanCreationException("can't find the version file api.version.properties path:" + path, e);
			} finally {
				if (null != ips) {
					try {
						ips.close();
					} catch (IOException e) {
						this.log.warn("close stream error", e);
					}
				}

			}
			
			return props;
		}

	}





	

}
