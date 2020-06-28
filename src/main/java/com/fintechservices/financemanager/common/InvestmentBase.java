package com.fintechservices.financemanager.common;

import com.fintechservices.financemanager.models.Ticker;
import org.apache.commons.math3.linear.RealMatrix;

import java.io.IOException;
import java.text.ParseException;

public abstract class InvestmentBase {
    public abstract RealMatrix getData() throws IOException, ParseException;

    public abstract InvestmentBase addTicker(Ticker ticker);
}
