package com.mthree.configs;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Configuration of Logger
 */
public class LoggerConfig {

    private static final Logger logger = Logger.getLogger(LoggerConfig.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("logs.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());

            logger.addHandler(fileHandler);
            logger.setLevel(Level.SEVERE);
            logger.setUseParentHandlers(true);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger", e);
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}