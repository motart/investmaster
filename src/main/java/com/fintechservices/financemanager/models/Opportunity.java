package com.fintechservices.financemanager.models;

import java.util.Map;

public class Opportunity {
    public double sharpeRatio;
    public Map<String, Double> weights;
    public double portfolioReturn;
    public double portfolioVariance;
    public double portfolioStDeviation;

    public String toString() {
        return "{Sharpe: " + sharpeRatio +",\n"+"weights: "+weights+"}";
    }
}
