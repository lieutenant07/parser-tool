package com.assignment.parser.config;

import com.assignment.parser.model.AccountData;
import com.univocity.parsers.common.DataProcessingException;
import com.univocity.parsers.common.RowProcessorErrorHandler;
import com.univocity.parsers.common.processor.BeanListProcessor;
import com.univocity.parsers.common.processor.RowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.fixed.FieldAlignment;
import com.univocity.parsers.fixed.FixedWidthFields;
import com.univocity.parsers.fixed.FixedWidthParser;
import com.univocity.parsers.fixed.FixedWidthParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ParserConfig {
    protected final static Logger log = LoggerFactory.getLogger(ParserConfig.class);

    public static final String HEADER_NAME = "Name";
    public static final String HEADER_ADDRESS = "Address";
    public static final String HEADER_POSTCODE = "Postcode";
    public static final String HEADER_PHONE = "Phone";
    public static final String HEADER_CREDIT_LIMIT = "Credit Limit";
    public static final String HEADER_BIRTHDAY = "Birthday";

    public static final char CHAR_WHITESPACE = ' ';
    public static final char CHAR_NO_PADDOING = '0';

    @Bean
    public FixedWidthFields fixedWidthField() {
        FixedWidthFields fields = new FixedWidthFields();

        fields.addField(HEADER_NAME, 16, FieldAlignment.LEFT, CHAR_WHITESPACE);
        fields.addField(HEADER_ADDRESS, 22, FieldAlignment.LEFT, CHAR_WHITESPACE);
        fields.addField(HEADER_POSTCODE, 9, FieldAlignment.LEFT, CHAR_WHITESPACE);
        fields.addField(HEADER_PHONE, 14, FieldAlignment.LEFT, CHAR_WHITESPACE);
        fields.addField(HEADER_CREDIT_LIMIT, 13, FieldAlignment.RIGHT, CHAR_WHITESPACE);
        fields.addField(HEADER_BIRTHDAY, 8, FieldAlignment.LEFT, CHAR_NO_PADDOING);
        return fields;
    }

    @Bean
    public BeanListProcessor<AccountData> accountDataListProcessor() {
        return new BeanListProcessor<>(AccountData.class);
    }

    @Bean
    public FixedWidthParserSettings accountDataFWParserSettings(FixedWidthFields fwFields, RowProcessor rowProcessor) {
        FixedWidthParserSettings parserSettings = new FixedWidthParserSettings(fwFields);

        parserSettings.setRowProcessor(rowProcessor);
        parserSettings.setHeaderExtractionEnabled(true);
        parserSettings.setRecordEndsOnNewline(true);

        return parserSettings;
    }

    @Bean
    public FixedWidthParser accountDataFWParser(FixedWidthParserSettings fwParserSettings) {
        return new FixedWidthParser(fwParserSettings);
    }

    @Bean
    public CsvParserSettings accountDataCSVParserSettings(RowProcessor rowProcessor, RowProcessorErrorHandler errorHandler) {
        CsvParserSettings parserSettings = new CsvParserSettings();
        parserSettings.getFormat().setDelimiter(',');
        parserSettings.getFormat().setQuote('"');
        parserSettings.setHeaderExtractionEnabled(true);
        parserSettings.setRowProcessor(rowProcessor);
        parserSettings.setRowProcessorErrorHandler(errorHandler);
        return parserSettings;
    }

    @Bean
    public CsvParser accountDataCSVParser(CsvParserSettings csvParserSettings) {
        return new CsvParser(csvParserSettings);
    }

    @Bean
    public RowProcessorErrorHandler accountDataErrorHandler() {
        return (error, inputRow, context) -> {
            log.error("Error processing row: " + Arrays.toString(inputRow));
            log.error("Error details: column '" + error.getColumnName() + "' (index " + error.getColumnIndex() + ") has value '" + inputRow[error.getColumnIndex()] + "'");
        };
    }
}
