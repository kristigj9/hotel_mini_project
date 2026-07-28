package com.lhind.hotel.exception;

public class RoomCapacityExceededException extends RuntimeException {

    public RoomCapacityExceededException(String message) {
        super(message);
    }
}