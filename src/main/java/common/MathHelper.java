package common;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.Date;
import java.util.TreeMap;

public class MathHelper {
    public static Double calculateAverageReturn( TreeMap<Date, Float> returns ) {
        Double result;
        result = returns.values().stream().mapToDouble(num -> Double.valueOf(num)).average().orElse(0);
        return result;
    }

    public static Double calculateVariance( TreeMap<Date, Double> returns ) {
        double [] testData = new double[ returns.size() ];
        for (int i=0; i < returns.size(); i++ ) {
            testData[i] = (double) returns.values().toArray()[i];
        }
        DescriptiveStatistics statistics = new DescriptiveStatistics( testData );
        return statistics.getVariance();
    }

    public static Double calculateStandardDeviation( TreeMap<Date, Double> returns ) {
        double [] testData = new double[ returns.size() ];
        for (int i=0; i < returns.size(); i++ ) {
            testData[i] = (double) returns.values().toArray()[i];
        }
        DescriptiveStatistics statistics = new DescriptiveStatistics( testData );
        return statistics.getStandardDeviation();
    }
}
