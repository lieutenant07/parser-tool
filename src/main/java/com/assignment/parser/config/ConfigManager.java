package com.assignment.parser.config;

import com.assignment.parser.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class ConfigManager {
    public static final String CMDLINE_ARG_FILE_PATH = "filePath";
    public static final String CMDLINE_ARG_EXPORT_HTML = "exportHtml";
    public static final String CMDLINE_ARG_VERBOSE = "verbose";

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private Environment env;

    private ConfigurableEnvironment cenv;

    private GenericPropertySourcesPlaceholderConfigurer psc;

    @Autowired
    public ConfigManager(Environment env, ConfigurableEnvironment cenv, GenericPropertySourcesPlaceholderConfigurer psc) {
        this.env = env;
        this.cenv = cenv;
        this.psc = psc;
    }

    private String parseType;
    private String filePath;
    private boolean exportHTML;
    private boolean verbose;

    public String getParseType() {
        return parseType;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isExportHtml() {
        return exportHTML;
    }

    public boolean isVerbose() {
        return verbose;
    }

    @PostConstruct
    private void init() {
        logPropertiesFromAllPropertySources("init:"+this.getClass().getSimpleName());
    }

    public void logPropertiesFromAllPropertySources(String phase) {
        logger.debug("Logging all properties from all PropertySources, phase={}",phase);
        SpringUtils.logPropertiesFromEnvironmentAndPropertySources(psc, cenv, env, logger);
    }

    public void applyCmdLineArgOverrides(ApplicationArguments appArgs) {
        logger.info("Applying Command Line args to {}",this.getClass().getSimpleName());
        this.parseType = appArgs.getNonOptionArgs().get(0); //1st argument is always expected to be either 'parseCsv' or 'parsePrn'

        this.filePath = Objects.nonNull(appArgs.getOptionValues(CMDLINE_ARG_FILE_PATH)) ?
                appArgs.getOptionValues(CMDLINE_ARG_FILE_PATH).stream().findFirst().orElse(null) : null;

        this.exportHTML = appArgs.containsOption(CMDLINE_ARG_EXPORT_HTML);

        this.verbose = appArgs.containsOption(CMDLINE_ARG_VERBOSE);
    }
}
