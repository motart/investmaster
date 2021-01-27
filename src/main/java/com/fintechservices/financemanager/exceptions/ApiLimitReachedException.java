package com.fintechservices.financemanager.exceptions;

public class ApiLimitReachedException extends Throwable {
    public ApiLimitReachedException(String rawDataString) {
        super(rawDataString);
    }
}
