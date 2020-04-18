package com.assignment.parser.service;

import com.assignment.parser.config.ConfigManager;
import com.assignment.parser.model.AccountData;
import com.univocity.parsers.common.AbstractParser;
import com.univocity.parsers.common.CommonParserSettings;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.csv.CsvParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DelimitedParserService extends BaseParserService<AccountData>{

    private final CsvParser csvParser;

    @Autowired
    public DelimitedParserService(ConfigManager cfgManager, BeanListProcessor<AccountData> rowProcessor, CsvParser csvParser) {
        super(cfgManager, rowProcessor);
        this.csvParser = csvParser;
    }

    @Override
    protected AbstractParser<? extends CommonParserSettings<?>> getParser() {
        return this.csvParser;
    }
}
