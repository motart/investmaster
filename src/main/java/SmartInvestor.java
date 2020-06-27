import common.TooFewStocksException;
import models.Ticker;
import theories.Markowitz;
import java.io.IOException;
import java.text.ParseException;

public class SmartInvestor {
    public static void main( String[] args ) throws TooFewStocksException, IOException, ParseException {
        Ticker tesla = null;
        Ticker netflix = null;
        Ticker amazon = null;
        Ticker apple = null;
        Ticker google = null;

        try {
            google = new Ticker("GOOG");
            apple = new Ticker("AAPL");
            amazon = new Ticker("AMZN");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Markowitz testRun = new Markowitz( google );
        testRun.addTicker( apple ).addTicker( amazon ).addTicker( amazon );
        testRun.run();
        System.out.println("Done!!");

    }
}
