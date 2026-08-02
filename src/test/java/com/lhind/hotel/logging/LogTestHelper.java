package com.lhind.hotel.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.util.ArrayList;
import java.util.List;

public final class LogTestHelper {

    private LogTestHelper() {
    }

    /**
     * Kap eventet e krijuara nga një logger gjatë ekzekutimit
     * të action-it të dhënë.
     *
     * @param loggerName emri i plotë i logger-it
     * @param level niveli që do të përdoret gjatë testit
     * @param action kodi që krijon log-et
     * @return lista e eventeve të kapura
     */
    public static List<LogEvent> capture(
            String loggerName,
            Level level,
            Runnable action
    ) {
        LoggerContext context =
                (LoggerContext) LogManager.getContext(false);

        org.apache.logging.log4j.core.config.Configuration configuration =
                context.getConfiguration();

        CapturingAppender appender =
                new CapturingAppender(
                        "test-capture-" + loggerName
                );

        appender.start();

        LoggerConfig loggerConfig =
                configuration.getLoggerConfig(loggerName);

        boolean exactMatch =
                loggerConfig.getName().equals(loggerName);

        LoggerConfig testConfig;

        if (!exactMatch) {
            testConfig = new LoggerConfig(
                    loggerName,
                    level,
                    false
            );

            configuration.addLogger(
                    loggerName,
                    testConfig
            );
        } else {
            testConfig = loggerConfig;
        }

        Level originalLevel =
                testConfig.getLevel();

        testConfig.setLevel(level);

        testConfig.addAppender(
                appender,
                level,
                null
        );

        context.updateLoggers();

        try {
            action.run();
        } finally {
            testConfig.removeAppender(
                    appender.getName()
            );

            testConfig.setLevel(
                    originalLevel
            );

            if (!exactMatch) {
                configuration.removeLogger(
                        loggerName
                );
            }

            appender.stop();
            context.updateLoggers();
        }

        return List.copyOf(
                appender.getEvents()
        );
    }

    public static class CapturingAppender
            extends AbstractAppender {

        private final List<LogEvent> events =
                new ArrayList<>();

        public CapturingAppender(String name) {
            super(
                    name,
                    null,
                    PatternLayout.createDefaultLayout(),
                    true,
                    Property.EMPTY_ARRAY
            );
        }

        @Override
        public synchronized void append(
                LogEvent event
        ) {
            events.add(
                    event.toImmutable()
            );
        }

        public synchronized List<LogEvent> getEvents() {
            return new ArrayList<>(events);
        }
    }
}