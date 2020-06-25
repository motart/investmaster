import common.MathHelper;
import models.Ticker;
import theories.Markowitz;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;

public class SmartInvestor {
    public static void main( String[] args ) {
        Ticker ibm = new Ticker("IBM");
        Markowitz testRun = new Markowitz( ibm );
        TreeMap<Date, Double> testData = new TreeMap<>();

        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        testData.put(calendar.getTime(), (double) 7);

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        testData.put(calendar.getTime(), (double) 2);


        calendar.add(Calendar.DAY_OF_MONTH, 3);
        testData.put(calendar.getTime(), (double) 0);

        DecimalFormat format = new DecimalFormat("###.##############");
        // System.out.println( format.format( testRun.getAverageReturn(testRun.calculateReturns(testRun.getPrices( testRun.getData() )))));
        System.out.println( format.format( MathHelper.calculateStandardDeviation(testData)));
    }
}
