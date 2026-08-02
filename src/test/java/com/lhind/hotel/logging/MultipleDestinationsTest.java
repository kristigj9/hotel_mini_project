package com.lhind.hotel.logging;

import com.lhind.hotel.service.HotelServiceImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MultipleDestinationsTest {

    private static final Logger log =
            LogManager.getLogger(HotelServiceImpl.class);

    @Test
    void eventShouldReachBothDestinations() {

        String loggerName =
                HotelServiceImpl.class.getName();

        LoggerContext context =
                (LoggerContext) LogManager.getContext(false);

        org.apache.logging.log4j.core.config.Configuration configuration =
                context.getConfiguration();

        LoggerConfig parentConfig =
                configuration.getLoggerConfig(loggerName);

        boolean exactMatch =
                parentConfig.getName().equals(loggerName);

        LoggerConfig testConfig;

        if (!exactMatch) {
            testConfig = new LoggerConfig(
                    loggerName,
                    Level.INFO,
                    false
            );

            configuration.addLogger(
                    loggerName,
                    testConfig
            );
        } else {
            testConfig = parentConfig;
        }

        Level originalLevel =
                testConfig.getLevel();

        LogTestHelper.CapturingAppender firstAppender =
                new LogTestHelper.CapturingAppender(
                        "first-destination"
                );

        LogTestHelper.CapturingAppender secondAppender =
                new LogTestHelper.CapturingAppender(
                        "second-destination"
                );

        firstAppender.start();
        secondAppender.start();

        testConfig.setLevel(Level.INFO);

        testConfig.addAppender(
                firstAppender,
                Level.INFO,
                null
        );

        testConfig.addAppender(
                secondAppender,
                Level.INFO,
                null
        );

        context.updateLoggers();

        try {
            log.info(
                    "message-for-both-destinations"
            );

            List<String> firstMessages =
                    firstAppender.getEvents()
                            .stream()
                            .map(event ->
                                    event.getMessage()
                                            .getFormattedMessage()
                            )
                            .toList();

            List<String> secondMessages =
                    secondAppender.getEvents()
                            .stream()
                            .map(event ->
                                    event.getMessage()
                                            .getFormattedMessage()
                            )
                            .toList();

            assertTrue(
                    firstMessages.contains(
                            "message-for-both-destinations"
                    )
            );

            assertTrue(
                    secondMessages.contains(
                            "message-for-both-destinations"
                    )
            );

        } finally {
            testConfig.removeAppender(
                    firstAppender.getName()
            );

            testConfig.removeAppender(
                    secondAppender.getName()
            );

            testConfig.setLevel(originalLevel);

            if (!exactMatch) {
                configuration.removeLogger(loggerName);
            }

            firstAppender.stop();
            secondAppender.stop();

            context.updateLoggers();
        }
    }
}