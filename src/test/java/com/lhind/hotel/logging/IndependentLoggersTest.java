package com.lhind.hotel.logging;

import com.lhind.hotel.service.HotelServiceImpl;
import com.lhind.hotel.service.RoomServiceImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IndependentLoggersTest {

    private static final Logger hotelLog =
            LogManager.getLogger(
                    HotelServiceImpl.class
            );

    private static final Logger roomLog =
            LogManager.getLogger(
                    RoomServiceImpl.class
            );

    @Test
    void changingOneLoggerShouldNotAffectTheOther() {

        List<LogEvent> hotelEvents =
                LogTestHelper.capture(
                        HotelServiceImpl.class.getName(),
                        Level.DEBUG,
                        () -> {
                            hotelLog.debug("hotel-debug");
                            hotelLog.info("hotel-info");
                            hotelLog.warn("hotel-warn");
                            hotelLog.error("hotel-error");
                        }
                );

        List<LogEvent> roomEvents =
                LogTestHelper.capture(
                        RoomServiceImpl.class.getName(),
                        Level.ERROR,
                        () -> {
                            roomLog.debug("room-debug");
                            roomLog.info("room-info");
                            roomLog.warn("room-warn");
                            roomLog.error("room-error");
                        }
                );

        List<String> hotelMessages =
                hotelEvents.stream()
                        .map(event ->
                                event.getMessage()
                                        .getFormattedMessage()
                        )
                        .toList();

        List<String> roomMessages =
                roomEvents.stream()
                        .map(event ->
                                event.getMessage()
                                        .getFormattedMessage()
                        )
                        .toList();

        assertTrue(
                hotelMessages.contains("hotel-debug")
        );

        assertTrue(
                hotelMessages.contains("hotel-info")
        );

        assertTrue(
                hotelMessages.contains("hotel-warn")
        );

        assertTrue(
                hotelMessages.contains("hotel-error")
        );

        assertFalse(
                roomMessages.contains("room-debug")
        );

        assertFalse(
                roomMessages.contains("room-info")
        );

        assertFalse(
                roomMessages.contains("room-warn")
        );

        assertTrue(
                roomMessages.contains("room-error")
        );

        assertFalse(
                hotelMessages.contains("room-error")
        );

        assertFalse(
                roomMessages.contains("hotel-error")
        );
    }
}