package rujianbin.autoconfiguration.dubbo.scan;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.spring.ReferenceBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import rujianbin.autoconfiguration.dubbo.annotation.DubboService;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


@Component
public class DubboReferenceScan implements BeanFactoryPostProcessor, ApplicationContextAware, Ordered {

	private ApplicationContext applicationContext;
	
	private String scanPackages="rujianbin";



	@Override
	public int getOrder() {
		return 300000;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Scanner scanner = new Scanner((BeanDefinitionRegistry)beanFactory);
	    scanner.setResourceLoader(this.applicationContext);
	    List<String> packages = AutoConfigurationPackages.get(this.applicationContext);
	    System.out.println("dubboReferenceScan---->"+StringUtils.arrayToCommaDelimitedString(StringUtils.toStringArray(packages)));
	    if(StringUtils.isEmpty(scanPackages)){
	    	return;
	    }
	    scanner.scan(StringUtils.tokenizeToStringArray(this.scanPackages, ",; \t\n"));

	}

	class Scanner extends ClassPathBeanDefinitionScanner {
		private final Logger log = LoggerFactory.getLogger(Scanner.class);
		private BeanDefinitionRegistry registry;

		private Map<String, String> interface_version = new HashMap<String,String>();
		
		public Scanner(BeanDefinitionRegistry registry) {
			super(registry);
			this.registry = registry;
		}

		protected boolean isCandidateComponent(MetadataReader metadataReader) throws IOException {
			if (metadataReader.getClassMetadata().isInterface()) {
				AnnotationMetadata metadata = metadataReader.getAnnotationMetadata();
				if (metadata!=null && metadata.hasAnnotation(DubboService.class.getName())) {
					Map<String,Object> attrs = metadata.getAnnotationAttributes(DubboService.class.getName());

					if (attrs!=null && !StringUtils.isEmpty(attrs.get("version"))) {
						interface_version.put(metadataReader.getClassMetadata().getClassName(), attrs.get("version").toString());
					}
					return true;
				}
			}
			return false;
		}

		protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
			return true;
		}

		// basePackages要传入实现类的base包
		protected Set<BeanDefinitionHolder> doScan(String[] basePackages) {
			Set beanDefinitions = new LinkedHashSet();
			for (String basePackage : basePackages) {
				// 获取指定包下的所有class,由于isCandidateComponent方法，返回的是所有有DubboService注解接口的对应的所有实现类
				Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
				Properties props = new Properties();
				for (BeanDefinition candidate : candidates) {
					try {
						Class interfaceCz = Class.forName(candidate.getBeanClassName());


						String dubboReferenceName = StringUtils.uncapitalize(Class.forName(candidate.getBeanClassName()).getSimpleName()) + "DubboReferenceImpl";

						String version = interface_version.get(candidate.getBeanClassName());
						if (StringUtils.isEmpty(version)) {
							// 加载版本号文件
							if (props.getProperty("dubbo.service.version")==null) {
								props = loadProperties(interfaceCz);
							}
							version = props.getProperty("dubbo.service.version");
						}

						if (StringUtils.isEmpty(version)) {
							throw new BeanCreationException(String.format("create %s bean error version can't be null",
									new Object[] { candidate.getBeanClassName() }));
						}

						Map appBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(
								DubboReferenceScan.this.applicationContext, ApplicationConfig.class, false, false);
						ApplicationConfig applicationConfig = (ApplicationConfig) ((Map.Entry) appBeans.entrySet()
								.iterator().next()).getValue();

						RootBeanDefinition beanDefinition = new RootBeanDefinition();
						beanDefinition.setBeanClass(ReferenceBean.class);
						beanDefinition.setLazyInit(false);
						beanDefinition.getPropertyValues().addPropertyValue("application", applicationConfig);
						beanDefinition.getPropertyValues().addPropertyValue("id", dubboReferenceName);
						beanDefinition.getPropertyValues().addPropertyValue("interface", interfaceCz.getName());
						beanDefinition.getPropertyValues().addPropertyValue("version", version);

						this.log.info("【dubbo 接口注入】Init dubbo reference {} version {} interface {}",
								new Object[] { dubboReferenceName, version, candidate.getBeanClassName() });
						this.registry.registerBeanDefinition(dubboReferenceName, beanDefinition);
					} catch (Exception e) {
						throw new BeanCreationException(
								String.format("create %s bean error ", new Object[] { candidate.getBeanClassName() }),
								e);
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
					ips = DubboReferenceScan.this.applicationContext.getResource(path).getInputStream();
					this.log.info("loading api.version.properties form  path:{}", path);
					props.load(ips);

					if (null != ips) {
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
