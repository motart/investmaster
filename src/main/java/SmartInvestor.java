import common.DataSizeMismatchException;
import common.MathHelper;
import common.TooFewStocksException;
import models.Ticker;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import theories.Markowitz;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class SmartInvestor {
    public static void main( String[] args ) throws TooFewStocksException, IOException, ParseException {
        Ticker ibm = null;
        Ticker apple = null;
        Ticker amazon = null;
        try {
            ibm = new Ticker("IBM");
            apple = new Ticker("AAPL");
            amazon = new Ticker("AMZN");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Markowitz testRun = new Markowitz( ibm );
        testRun.addTicker( apple );
        testRun.addTicker( amazon );
        testRun.run();
        System.out.println("Done!!");

    }
}
