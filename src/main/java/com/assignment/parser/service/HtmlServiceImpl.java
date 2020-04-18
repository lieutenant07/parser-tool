package com.assignment.parser.service;

import com.assignment.parser.exception.ParserException;
import com.assignment.parser.model.AccountData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.assignment.parser.config.ParserConfig.HEADER_NAME;
import static com.assignment.parser.config.ParserConfig.HEADER_POSTCODE;
import static com.assignment.parser.config.ParserConfig.HEADER_ADDRESS;
import static com.assignment.parser.config.ParserConfig.HEADER_PHONE;
import static com.assignment.parser.config.ParserConfig.HEADER_CREDIT_LIMIT;
import static com.assignment.parser.config.ParserConfig.HEADER_BIRTHDAY;

@Service
public class HtmlServiceImpl implements HtmlService {
    public static final String TD_OPEN_TAG = "<td>";
    public static final String TD_CLOSE_TAG = "</td>";
    public static final String TH_OPEN_TAG = "<th>";
    public static final String TH_CLOSE_TAG = "</th>";
    public static final String TR_OPEN_TAG = "<tr>";
    public static final String TR_CLOSE_TAG = "</tr>";

    public static final String TABLE_HEAD_ELEMENT_NAME = "thead";
    public static final String TABLE_BODY_ELEMENT_NAME = "tbody";

    public static final String CHARSET_NAME_UTF8 = "UTF-8";

    public static final String HTML_TEMPLATE_PATH = "src/main/resources/htmlTemplate.html";
    public static final String HTML_TABLE_STUB = "<table><thead></thead><tbody></tbody></table>";

    @Override
    public String produceHtml(List<AccountData> accountDataList) throws IOException {
        Document htmlTemplateDoc = getHtmlTemplate();

        Document htmlTable = Jsoup.parseBodyFragment(HTML_TABLE_STUB);
        htmlTable.select(TABLE_HEAD_ELEMENT_NAME).append(getTHeadInnerHTMLString());
        accountDataList.stream()
                .forEach(t -> htmlTable.select(TABLE_BODY_ELEMENT_NAME)
                        .append(getTBodyInnerHTMLString(t)));

        htmlTemplateDoc.body().html(htmlTable.body().html());
        return htmlTemplateDoc.html();
    }

    @Override
    public void writeHtmlToFile(String htmlString) throws ParserException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH;mm;ss;SSS");
        LocalDateTime now = LocalDateTime.now();
        dtf.format(now);

        String filePath = new StringBuilder("output/htmlOutput-")
                .append(dtf.format(now))
                .append(".html")
                .toString();
        File file = new File(filePath);

        try(BufferedWriter fileWriter = new BufferedWriter(new FileWriter(file))) {
            fileWriter.write(htmlString);
        } catch (IOException ex) {
            throw new ParserException(String.format(
                    "An error has occurred when trying to save the file %s to the following path: %s"
                    , file.getName(), file.getPath()), ex);
        }
    }

    @Override
    public Document getHtmlTemplate() throws IOException {
        File file = new File(HTML_TEMPLATE_PATH);
        return Jsoup.parse(file, CHARSET_NAME_UTF8, "");
    }

    public String getTBodyInnerHTMLString(AccountData accountData) {
        return new StringBuilder()
                .append(TR_OPEN_TAG)
                .append(TD_OPEN_TAG).append(accountData.getName()).append(TD_CLOSE_TAG)
                .append(TD_OPEN_TAG).append(accountData.getAddress()).append(TD_CLOSE_TAG)
                .append(TD_OPEN_TAG).append(accountData.getPostCode()).append(TD_CLOSE_TAG)
                .append(TD_OPEN_TAG).append(accountData.getPhone()).append(TD_CLOSE_TAG)
                .append(TD_OPEN_TAG).append(String.format("%.00f" ,accountData.getCreditLimit())).append(TD_CLOSE_TAG)
                .append(TD_OPEN_TAG).append(accountData.getBirthday()).append(TD_CLOSE_TAG)
                .append(TR_CLOSE_TAG)
                .toString();

    }

    public String getTHeadInnerHTMLString() {
        return new StringBuilder()
                .append(TR_OPEN_TAG)
                .append(TH_OPEN_TAG).append(HEADER_NAME).append(TH_CLOSE_TAG)
                .append(TH_OPEN_TAG).append(HEADER_POSTCODE).append(TH_CLOSE_TAG)
                .append(TH_OPEN_TAG).append(HEADER_ADDRESS).append(TH_CLOSE_TAG)
                .append(TH_OPEN_TAG).append(HEADER_PHONE).append(TH_CLOSE_TAG)
                .append(TH_OPEN_TAG).append(HEADER_CREDIT_LIMIT).append(TH_CLOSE_TAG)
                .append(TH_OPEN_TAG).append(HEADER_BIRTHDAY).append(TH_CLOSE_TAG)
                .append(TR_CLOSE_TAG)
                .toString();
    }
}
