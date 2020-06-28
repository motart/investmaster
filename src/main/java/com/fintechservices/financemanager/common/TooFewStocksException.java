package com.fintechservices.financemanager.common;

public class TooFewStocksException extends Exception {
    public TooFewStocksException(String s) {
        super(s);
    }
}
