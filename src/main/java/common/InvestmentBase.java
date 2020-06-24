package common;

import models.Ticker;

public abstract class InvestmentBase {
    public abstract String getData();

    public abstract InvestmentBase addTicker(Ticker ticker);
}
