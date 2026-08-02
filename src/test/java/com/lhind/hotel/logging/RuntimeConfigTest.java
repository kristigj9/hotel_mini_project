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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RuntimeConfigTest {

    private static final Logger log =
            LogManager.getLogger(HotelServiceImpl.class);

    @Test
    void shouldChangeLoggerLevelAtRuntime() {

        String loggerName =
                HotelServiceImpl.class.getName();

        List<LogEvent> events =
                LogTestHelper.capture(
                        loggerName,
                        Level.INFO,
                        () -> {
                            log.debug("debug-before-change");
                            log.info("info-before-change");

                            changeLevel(
                                    loggerName,
                                    Level.DEBUG
                            );

                            log.debug("debug-after-change");
                            log.info("info-after-change");

                            changeLevel(
                                    loggerName,
                                    Level.ERROR
                            );

                            log.warn("warn-after-error-level");
                            log.error("error-after-error-level");
                        }
                );

        List<String> messages = events.stream()
                .map(event ->
                        event.getMessage()
                                .getFormattedMessage()
                )
                .toList();

        assertFalse(
                messages.contains("debug-before-change")
        );

        assertTrue(
                messages.contains("info-before-change")
        );

        assertTrue(
                messages.contains("debug-after-change")
        );

        assertTrue(
                messages.contains("info-after-change")
        );

        assertFalse(
                messages.contains("warn-after-error-level")
        );

        assertTrue(
                messages.contains("error-after-error-level")
        );
    }

    private void changeLevel(
            String loggerName,
            Level level
    ) {
        LoggerContext context =
                (LoggerContext) LogManager.getContext(false);

        org.apache.logging.log4j.core.config.Configuration configuration =
                context.getConfiguration();

        LoggerConfig loggerConfig =
                configuration.getLoggerConfig(loggerName);

        if (!loggerConfig.getName().equals(loggerName)) {
            LoggerConfig exactLoggerConfig =
                    new LoggerConfig(
                            loggerName,
                            level,
                            false
                    );

            configuration.addLogger(
                    loggerName,
                    exactLoggerConfig
            );
        } else {
            loggerConfig.setLevel(level);
        }

        context.updateLoggers();
    }
}