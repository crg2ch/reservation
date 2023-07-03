package com.minsoo.reservation.common.exception;

public class ExistsEmailException extends RuntimeException {
    public ExistsEmailException(String s) {
        super(s);
    }
}
