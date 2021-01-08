package com.fintechservices.financemanager.models;

import java.util.Map;

public class Opportunity {
    public double sharpeRatio;
    public Map<String, Double> weights;
    public double portfolioReturn;
    public double portfolioVariance;
    public double portfolioStDeviation;

    @Override
    public String toString() {
        return "Opportunity{" +
                "sharpeRatio=" + sharpeRatio + "\n" +
                " weights=" + weights + "\n" +
                " portfolioReturn=" + portfolioReturn + "\n" +
                " portfolioVariance=" + portfolioVariance + "\n" +
                " portfolioStDeviation=" + portfolioStDeviation + "\n" +
                '}';
    }
}
