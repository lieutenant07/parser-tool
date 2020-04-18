package com.assignment.parser.utils;

import org.slf4j.Logger;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.*;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.TreeMap;

public class SpringUtils {

    public static final String ENVIRONMENT_LOCAL_PROPERTIES = "localProperties";

    public static String getFormattedPropsStringFromPropertySource(PropertySource ps, String delim, StringBuilder sb) {
        sb = (sb != null) ? sb : new StringBuilder() ;

        Map<String, Object> propsMap = new TreeMap<>();
        Map<String, Object> passwordMap = new TreeMap<>();

        if (ps instanceof MapPropertySource) {
            propsMap.putAll( ((MapPropertySource) ps).getSource() );
        } else if (ps instanceof SimpleCommandLinePropertySource) {
            SimpleCommandLinePropertySource src = (SimpleCommandLinePropertySource) ps;
            src.getSource();
        }
        for (Map.Entry<String, Object> property : propsMap.entrySet()) {
            if(property.getKey().toLowerCase().contains("password") || property.getKey().toLowerCase().contains("passwd")){
                passwordMap.put(property.getKey(), org.apache.commons.lang3.StringUtils.repeat("*", property.getValue().toString().length()));
            }
        }
        for (Map.Entry<String, Object> property : passwordMap.entrySet()) {
            propsMap.put(property.getKey(), property.getValue());
        }

        sb.append("Env PropertySource: ").append(ps.getName()).append(", with props: \n\t")
                .append(StringUtils.collectionToDelimitedString(propsMap.entrySet(), org.apache.commons.lang3.StringUtils.isEmpty(delim) ? "," : delim)).append("\n\n");

        return sb.toString();
    }

    public static String getFormattedPropsStringFromEnvPropertySources(Environment env, String delim) {
        StringBuilder sb = new StringBuilder();

        for (PropertySource<?> propSrc : ((AbstractEnvironment) env).getPropertySources()) {
            getFormattedPropsStringFromPropertySource(propSrc,delim,sb);
        }

        return sb.toString();
    }

    public static void logPropertiesFromEnvironmentAndPropertySources(PropertySourcesPlaceholderConfigurer psc, ConfigurableEnvironment cenv, Environment env, Logger logger) {
        for (PropertySource<?> propertySource : psc.getAppliedPropertySources()) {
            if (propertySource.getName()
                    .equals(ENVIRONMENT_LOCAL_PROPERTIES)) { //put local properties only to Spring Environment
                logger.info("ApplicationConfig loaded properties for PropertySource: {}", propertySource.getName());
                cenv.getPropertySources().addFirst(propertySource);
            } else {
                logger.trace("Skipping applied PropertySource: {}", propertySource.getName());
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("\n %s Resulting Spring Environment properties %s\n", org.apache.commons.lang3.StringUtils.repeat("-", 32), org.apache.commons
                .lang3.StringUtils.repeat("-", 32)));
        sb.append(SpringUtils.getFormattedPropsStringFromEnvPropertySources(env,"\n\t"));
        sb.append("\n").append(org.apache.commons.lang3.StringUtils.repeat("-", 105));

        logger.info("{}",sb.toString());
    }
}
