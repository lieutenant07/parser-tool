package com.assignment.parser.command;

import com.assignment.parser.config.ConfigManager;
import com.assignment.parser.exception.ParserException;
import com.assignment.parser.model.AccountData;
import com.assignment.parser.service.HtmlServiceImpl;
import com.assignment.parser.service.ReaderService;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ParserCommand implements Command {
    protected final static Logger log = LoggerFactory.getLogger(ParserCommand.class);

    public static final String CMDLINE_ARG_PARSE_CSV = "parseCsv";
    public static final String CMDLINE_ARG_PARSE_PRN = "parsePrn";
    public static final String DELIMITER = "/";

    public static final String FILE_EXTENSION_CSV = "csv";
    public static final String FILE_EXTENSION_PRN = "prn";

    private ConfigManager cfgManager;
    private ReaderService readerService;
    private HtmlServiceImpl htmlService;

    @Autowired
    public ParserCommand(ConfigManager cfgManager, ReaderService readerService, HtmlServiceImpl htmlService) {
        this.cfgManager = cfgManager;
        this.readerService = readerService;
        this.htmlService = htmlService;
    }

    @Override
    public void execute() throws IOException,ParserException {
        log.info("Beginning data processing...");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final String parseType = Optional.ofNullable(cfgManager.getParseType())
                .orElseThrow(() -> new ParserException(
                        String.format("Passed cmd line arguments are not valid, 'parseType' cmd line arg has to be the first one used, with allowed values of %s.",
                                new StringBuilder(CMDLINE_ARG_PARSE_CSV).append(DELIMITER).append(CMDLINE_ARG_PARSE_PRN))));
        final String filePath = Optional.ofNullable(cfgManager.getFilePath())
                .orElseThrow(() -> new ParserException(
                        "Path to the source file has not been provided! Please consider adding 'filePath' non-option arg."));
        validateFileExtension(filePath);

        final List<AccountData> accountDataList;
        //try-with-resources
        try(Reader reader = new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8)) {
            log.info("Processing the file {} in location {} started.", cfgManager.getParseType(), filePath);

            switch (parseType) {
                case CMDLINE_ARG_PARSE_CSV:
                    accountDataList = readerService.parseCsv(reader);
                    break;
                case CMDLINE_ARG_PARSE_PRN:
                    accountDataList = readerService.parsePrn(reader);
                    break;
                default:
                    throw new ParserException(
                            String.format("Passed cmd line arguments are not valid! Expected 1st arg is: %s. Actual: %s",
                                    new StringBuilder(CMDLINE_ARG_PARSE_CSV).append(DELIMITER).append(CMDLINE_ARG_PARSE_PRN),
                                    cfgManager.getParseType()));
            }
        } catch (IOException ex) {
            throw new ParserException(String.format(
                    "An error has occurred when trying to read the file in the following path: %s"
                    , filePath), ex);
        }

        stopWatch.stop();
        log.info("Processing the file {} in location {} ended.", cfgManager.getParseType(), filePath);

        long millis = stopWatch.getTotalTimeMillis();
        log.info("Time elapsed: {}", String.format("%02d:%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
                millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis))));

        log.debug("Serializing account data into HTML...");
        final String htmlString = htmlService.produceHtml(accountDataList);
        if(cfgManager.isVerbose()) {
            log.debug("Parsed data for the file {} has been de-serialized into following AccountData records:", filePath);
            accountDataList.stream().forEach(t -> log.debug(t.toString()));

            log.debug("HTML account data output:\n {}", htmlString);
        }

        if(cfgManager.isExportHtml()) {
            htmlService.writeHtmlToFile(htmlString);
        }
        log.info("Ended data processing.");
    }

    protected void validateFileExtension(String filePath) throws ParserException {
        if(FILE_EXTENSION_CSV.equalsIgnoreCase(FilenameUtils.getExtension(filePath)) ||
                FILE_EXTENSION_PRN.equalsIgnoreCase(FilenameUtils.getExtension(filePath))) {
            log.debug("File extension for the file named {} in location {} has been successfully identified as {}",
                    FilenameUtils.getName(filePath),
                    FilenameUtils.getPath(filePath),
                    FilenameUtils.getExtension(filePath));
        } else throw new ParserException(
                String.format("Extension of the file in the following location: %s, is invalid! Actual extension: %s, expected: %s.",
                        filePath,
                        FilenameUtils.getExtension(filePath),
                        new StringBuilder(FILE_EXTENSION_CSV).append(DELIMITER).append(FILE_EXTENSION_PRN).toString()));
    }
}
