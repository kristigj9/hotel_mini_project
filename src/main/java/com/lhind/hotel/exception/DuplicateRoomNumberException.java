package com.lhind.hotel.exception;

public class DuplicateRoomNumberException extends RuntimeException {

    public DuplicateRoomNumberException(String message) {
        super(message);
    }
}