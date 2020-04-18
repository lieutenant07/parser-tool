package com.assignment.parser.service;

import com.assignment.parser.config.ConfigManager;
import com.assignment.parser.model.AccountData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ReaderServiceImpl implements ReaderService {

    private ConfigManager configManager;
    private FixedWidthParserService fwParserSvc;
    private DelimitedParserService csvParserSvc;

    @Autowired
    public ReaderServiceImpl(ConfigManager configManager, FixedWidthParserService fwParserSvc, DelimitedParserService csvParserSvc) {
        this.configManager = configManager;
        this.fwParserSvc = fwParserSvc;
        this.csvParserSvc = csvParserSvc;
    }

    @Override
    public List<AccountData> parseCsv(Reader reader) {
        return csvParserSvc.parse(reader);
    }

    @Override
    public List<AccountData> parsePrn(Reader reader) {
        return fwParserSvc.parse(reader);
    }

}
