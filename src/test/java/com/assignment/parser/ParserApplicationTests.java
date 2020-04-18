package com.assignment.parser;

import com.assignment.parser.exception.ParserException;
import com.assignment.parser.model.AccountData;
import com.assignment.parser.service.HtmlService;
import com.assignment.parser.service.ReaderService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.assignment.parser.service.HtmlServiceImpl.CHARSET_NAME_UTF8;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
class ParserApplicationTests {
	public static final String CSV_FILE_PATH = "src/test/resources/input/data.csv";
	public static final String PRN_FILE_PATH = "src/test/resources/input/data.prn";
	public static final String CSV_HTML_VALID_OUTPUT_PATH = "src/test/resources/output/csvToHtmlOutput.html";
	public static final String PRN_HTML_VALID_OUTPUT_PATH = "src/test/resources/output/prnToHtmlOutput.html";

	ReaderService readerService;
	HtmlService htmlService;

	@Autowired
	public ParserApplicationTests(ReaderService readerService, HtmlService htmlService) {
		this.readerService = readerService;
		this.htmlService = htmlService;
	}

	@Test
	void retrieveHtmlFromCsv() throws IOException {
		final List<AccountData> accountDataList;
		try (Reader reader = new InputStreamReader(
				new FileInputStream(CSV_FILE_PATH), StandardCharsets.UTF_8)) {
			accountDataList = readerService.parseCsv(reader);
		}
		final String htmlString = htmlService.produceHtml(accountDataList);
		final Document producedHtml = Jsoup.parse(htmlString);

		File file = new File(CSV_HTML_VALID_OUTPUT_PATH);
		final Document expectedHtml = Jsoup.parse(file, CHARSET_NAME_UTF8, "");

		expectedHtml.hasSameValue(htmlString);

		assertTrue(expectedHtml.hasSameValue(producedHtml));
	}

	@Test
	void retrieveHtmlFromPrn() throws IOException {
		final List<AccountData> accountDataList;
		try (Reader reader = new InputStreamReader(
				new FileInputStream(PRN_FILE_PATH), StandardCharsets.UTF_8)) {
			accountDataList = readerService.parsePrn(reader);
		}
		final String htmlString = htmlService.produceHtml(accountDataList);
		final Document producedHtml = Jsoup.parse(htmlString);

		File file = new File(PRN_HTML_VALID_OUTPUT_PATH);
		final Document expectedHtml = Jsoup.parse(file, CHARSET_NAME_UTF8, "");

		expectedHtml.hasSameValue(htmlString);

		assertTrue(expectedHtml.hasSameValue(producedHtml));
	}

}
