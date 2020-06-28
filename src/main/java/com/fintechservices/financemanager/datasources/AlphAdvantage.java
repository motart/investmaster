package com.fintechservices.financemanager.datasources;

public class AlphAdvantage {
    private static final String[] iterval = {"1min","5min","15min","30min","60min"};
    private enum function {
        TIME_SERIES_INTRADAY, TIME_SERIES_DAILY, TIME_SERIES_DAILY_ADJUSTED, TIME_SERIES_WEEKLY
    }
    private String url;
    private String key;
}
