package com.assignment.parser.service;

import com.assignment.parser.config.ConfigManager;
import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.common.CommonParserSettings;
import com.univocity.parsers.common.processor.BeanListProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Reader;
import java.util.List;

public abstract class BaseParserService<T> {
    protected final static Logger log = LoggerFactory.getLogger(BaseParserService.class);

    private final ConfigManager cfgManager;
    private final BeanListProcessor<T> rowProcessor;

    @Autowired
    public BaseParserService(ConfigManager cfgManager, BeanListProcessor<T> rowProcessor) {
        this.cfgManager = cfgManager;
        this.rowProcessor = rowProcessor;
    }

    public List<T> parse(Reader reader) {
        getParser().parse(reader);
        return getRowProcessor().getBeans();
    }

    protected abstract AbstractParser<? extends CommonParserSettings<?>> getParser();

    protected BeanListProcessor<T> getRowProcessor(){
        return this.rowProcessor;
    }

}
