package models;

public class Ticker {
    private static String symbol;
    private Float averageReturn;
    private Float variance;
    private Float standardDeviation;

    public Ticker( String symbol) {
        Ticker.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
