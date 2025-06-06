// src/main/java/com/nexsys/util/LoggingUtils.java
package com.nexsys.util;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.core.Appender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.LoggerFactory;

/**
 * Logging utilities for async logging configuration.
 */
public final class LoggingUtils {
    private LoggingUtils() {}

    /**
     * Activate async logging to prevent blocking I/O.
     */
    public static void activateAsyncLogging() {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        // Configure async appender settings
        context.getLoggerList().forEach(logger -> {
            logger.iteratorForAppenders().forEachRemaining(appender -> {
                if (appender instanceof AsyncAppender asyncAppender) {
                    asyncAppender.setQueueSize(512);
                    asyncAppender.setDiscardingThreshold(0);
                    asyncAppender.setIncludeCallerData(false);
                }
            });
        });
    }
}