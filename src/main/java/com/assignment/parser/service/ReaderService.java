package com.assignment.parser.service;

import com.assignment.parser.model.AccountData;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

public interface ReaderService {
    List<AccountData> parseCsv(Reader reader) throws IOException;
    List<AccountData> parsePrn(Reader reader) throws IOException;
}
