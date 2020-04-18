package com.assignment.parser.config;

import com.assignment.parser.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Component
public class GenericPropertySourcesPlaceholderConfigurer extends PropertySourcesPlaceholderConfigurer
		implements InitializingBean {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	// This is called earlier than postProcessBeanFactory
	public void afterPropertiesSet() {
		try {
			Properties loadedProperties = this.mergeProperties();
			for (Map.Entry<Object, Object> singleProperty : loadedProperties.entrySet()) {
				if(singleProperty.getKey().toString().toLowerCase().contains("password") || singleProperty.getKey().toString().toLowerCase().contains("passwd")){
					logger.trace("AfterPropertiesSet loaded property: {}={} ", singleProperty.getKey(), StringUtils.repeat("*", singleProperty.getValue().toString().length()));

				}else{
					logger.trace("AfterPropertiesSet loaded property: {}={} ", singleProperty.getKey(), singleProperty.getValue());
				}
			}
		} catch (Exception ex) {
			// Ignore this exception, just log error
			logger.error("Unable to display loaded properties due: ", ex);
		}
	}

	@Override
	public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
		super.postProcessBeanFactory(beanFactory);

		super.getAppliedPropertySources().forEach(propertySource -> {
			if (propertySource.getSource() instanceof Map) {
				logger.debug("Loaded PropertySource: {}, with properties: \n\t{}", propertySource.getName(),
						SpringUtils.getFormattedPropsStringFromPropertySource(propertySource,",\n\t",null));
			}
		});
	}

}

