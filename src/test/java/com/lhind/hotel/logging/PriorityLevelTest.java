package com.lhind.hotel.logging;

import com.lhind.hotel.service.HotelServiceImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriorityLevelTest {

    private static final Logger log =
            LogManager.getLogger(HotelServiceImpl.class);

    @Test
    void infoLevelShouldCaptureInfoAndHigherLevels() {

        List<LogEvent> events =
                LogTestHelper.capture(
                        HotelServiceImpl.class.getName(),
                        Level.INFO,
                        () -> {
                            log.trace("trace-message");
                            log.debug("debug-message");
                            log.info("info-message");
                            log.warn("warn-message");
                            log.error("error-message");
                            log.fatal("fatal-message");
                        }
                );

        List<String> messages = extractMessages(events);

        assertFalse(messages.contains("trace-message"));
        assertFalse(messages.contains("debug-message"));

        assertTrue(messages.contains("info-message"));
        assertTrue(messages.contains("warn-message"));
        assertTrue(messages.contains("error-message"));
        assertTrue(messages.contains("fatal-message"));
    }

    @Test
    void debugLevelShouldCaptureDebugAndHigherLevels() {

        List<LogEvent> events =
                LogTestHelper.capture(
                        HotelServiceImpl.class.getName(),
                        Level.DEBUG,
                        () -> {
                            log.trace("trace-message");
                            log.debug("debug-message");
                            log.info("info-message");
                            log.warn("warn-message");
                            log.error("error-message");
                            log.fatal("fatal-message");
                        }
                );

        List<String> messages = extractMessages(events);

        assertFalse(messages.contains("trace-message"));

        assertTrue(messages.contains("debug-message"));
        assertTrue(messages.contains("info-message"));
        assertTrue(messages.contains("warn-message"));
        assertTrue(messages.contains("error-message"));
        assertTrue(messages.contains("fatal-message"));
    }

    @Test
    void traceLevelShouldCaptureAllLevels() {

        List<LogEvent> events =
                LogTestHelper.capture(
                        HotelServiceImpl.class.getName(),
                        Level.TRACE,
                        () -> {
                            log.trace("trace-message");
                            log.debug("debug-message");
                            log.info("info-message");
                            log.warn("warn-message");
                            log.error("error-message");
                            log.fatal("fatal-message");
                        }
                );

        List<String> messages = extractMessages(events);

        assertTrue(messages.contains("trace-message"));
        assertTrue(messages.contains("debug-message"));
        assertTrue(messages.contains("info-message"));
        assertTrue(messages.contains("warn-message"));
        assertTrue(messages.contains("error-message"));
        assertTrue(messages.contains("fatal-message"));
    }

    @Test
    void errorLevelShouldCaptureOnlyErrorAndFatal() {

        List<LogEvent> events =
                LogTestHelper.capture(
                        HotelServiceImpl.class.getName(),
                        Level.ERROR,
                        () -> {
                            log.trace("trace-message");
                            log.debug("debug-message");
                            log.info("info-message");
                            log.warn("warn-message");
                            log.error("error-message");
                            log.fatal("fatal-message");
                        }
                );

        List<String> messages = extractMessages(events);

        assertFalse(messages.contains("trace-message"));
        assertFalse(messages.contains("debug-message"));
        assertFalse(messages.contains("info-message"));
        assertFalse(messages.contains("warn-message"));

        assertTrue(messages.contains("error-message"));
        assertTrue(messages.contains("fatal-message"));
    }

    private List<String> extractMessages(
            List<LogEvent> events
    ) {
        return events.stream()
                .map(event ->
                        event.getMessage()
                                .getFormattedMessage()
                )
                .toList();
    }
}