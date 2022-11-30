package com.github.sokumbhar.MyApplication.service;

import com.interview.anandankush0908.loganalyzer.validator.LogAnalyzerValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

@Service
public class LogAnalyzerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAnalyzerService.class);

    @Autowired
    private LogAnalyzerValidator logAnalyzerValidator;

    @Autowired
    private LogParsingPersistingService logParsingPersistingService;

    public void analyzeLogs(String []args) throws URISyntaxException {
        boolean isValid = logAnalyzerValidator.validateInput(args);
        if(isValid) {
            try {
                logParsingPersistingService.parseAndPersistEvents(ClassLoader.getSystemResource(args[0]).getPath());
            } catch(IOException ex) {
                LOGGER.error("An exception occurred while parsing and persisting the file and the error is: {}", ex.getMessage());
            }
        }
        else
            throw new RuntimeException("Invalid Input");
    }
}
