package com.assignment.parser.service;

import com.assignment.parser.config.ConfigManager;
import com.assignment.parser.model.AccountData;
import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.common.CommonParserSettings;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.fixed.FixedWidthParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FixedWidthParserService extends BaseParserService<AccountData>{

    private final FixedWidthParser fwParser;

    @Autowired
    public FixedWidthParserService(ConfigManager cfgManager, BeanListProcessor<AccountData> rowProcessor, FixedWidthParser fwParser) {
        super(cfgManager, rowProcessor);
        this.fwParser = fwParser;
    }

    @Override
    protected AbstractParser<? extends CommonParserSettings<?>> getParser() {
        return this.fwParser;
    }

}
