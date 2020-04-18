package com.assignment.parser.service;

import com.assignment.parser.exception.ParserException;
import com.assignment.parser.model.AccountData;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public interface HtmlService {
    Document getHtmlTemplate() throws IOException;
    String produceHtml(List<AccountData> accountDataList) throws IOException;
    void writeHtmlToFile(String htmlString) throws ParserException;

}
