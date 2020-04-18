package com.assignment.parser.command;

import com.assignment.parser.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Arrays;

@Profile("!test")
@Component
public class ParserCommandExecutor implements ApplicationRunner, ApplicationContextAware {
    protected final static Logger log = LoggerFactory.getLogger(ParserCommandExecutor.class);
    public final static String APP_NAME = "Parser Tool";

    private static ApplicationContext appCtx;
    private ConfigManager cfgmgr;
    private ParserCommand parserCommand;

    @Autowired
    public ParserCommandExecutor(ConfigManager cfgmgr, ParserCommand parserCommand) {
        this.cfgmgr = cfgmgr;
        this.parserCommand = parserCommand;
    }

    @Override
    public void run(ApplicationArguments appArgs) throws Exception {
        printAppCtxBeans(appCtx);

        log.info("Launching {} application services", APP_NAME);
        log.debug("Passed cmd raw args: {}", Arrays.asList(appArgs.getSourceArgs()));
        log.debug("Passed cmd options: {}",appArgs.getOptionNames());
        log.debug("Passed cmd non-options: {}",appArgs.getNonOptionArgs());

        cfgmgr.applyCmdLineArgOverrides(appArgs);

        log.info("Executing parser commands...");
        parserCommand.execute();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }

    @PreDestroy
    public void destroy() {
        log.info("{} closing...", APP_NAME);
    }

    private static void printAppCtxBeans(ApplicationContext appCtx) {
        String[] beanNames = appCtx.getBeanDefinitionNames();
        StringBuilder sb = new StringBuilder(beanNames.length * 64);
        Arrays.sort(beanNames);
        int i = 1;
        for (String beanName : beanNames) {
            sb.append(String.format("%03d: %s", i, beanName)).append("\n");
            i++;
        }

        log.debug("Beans (#{}) provided by Spring Boot:\n{}", beanNames.length, sb.toString());
    }
}
