package com.fintechservices.financemanager.common;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class MathHelper {
    static Covariance covariance = new Covariance();
    static StandardDeviation standardDeviation = new StandardDeviation();
    public static Double calculateAverageReturn( TreeMap< Date, Double > returns ) {
        double result;
        result = returns.values().stream().mapToDouble(num -> num).average().orElse(0);
        return result;
    }

    public static Double calculateVariance( TreeMap<Date, Double> returns ) {
        double[] testData = convertTreeMapToArray( returns );
        for (int i=0; i < returns.size(); i++ ) {
            testData[i] = (double) returns.values().toArray()[i];
        }
        DescriptiveStatistics statistics = new DescriptiveStatistics( testData );
        return statistics.getVariance();
    }

    public static double[] convertMapToArray(Map<String, Double> source) {
        double[] result = new double[source.size()];
        for (int i=0; i < source.size(); i++ ) {
            result[i] = (double) source.values().toArray()[i];
        }
        return result;
    }

    public static double[] convertTreeMapToArray(TreeMap<Date, Double> source) {
        double[] result = new double[source.size()];
        for (int i=0; i < source.size(); i++ ) {
            result[i] = (double) source.values().toArray()[i];
        }
        return result;
    }

    public static TreeMap<Date, Double> calculateReturns( TreeMap<Date,Double> stockPrices ) {
        Double previousValue = null;
        Date previousDate = null;
        TreeMap<Date, Double> processedData = new TreeMap<>();
        for ( Map.Entry<Date, Double> entry: stockPrices.entrySet() ) {
            if ( previousValue == null ) {
                previousDate = entry.getKey();
                previousValue = entry.getValue();
            } else {
                processedData.put(previousDate, (entry.getValue() - previousValue) / previousValue);
                previousDate = entry.getKey();
                previousValue = entry.getValue();
            }
        }
        return processedData;
    }

    public static RealMatrix calculateCovariance (RealMatrix data ) {
        return new Covariance( data ).getCovarianceMatrix();
    }

    public static double calculateSquareRoot (Double value ) {
        return Math.sqrt( value );
    }

    public static Double calculateStandardDeviation( TreeMap<Date, Double> returns ) {
        double [] testData = new double[ returns.size() ];
        for (int i=0; i < returns.size(); i++ ) {
            testData[i] = (double) returns.values().toArray()[i];
        }
        DescriptiveStatistics statistics = new DescriptiveStatistics( testData );
        return statistics.getStandardDeviation();
    }

    public static Double calculateCovariance ( TreeMap<Date, Double> stockAReturns, TreeMap<Date, Double> stockBReturns ) throws DataSizeMismatchException {
        if (stockAReturns.size() != stockBReturns.size() ) {
            throw new DataSizeMismatchException("Data sizes are not the same");
        }
        double [] stockA = new double[ stockAReturns.size() ];
        double [] stockB = new double[ stockBReturns.size() ];
        for (int i=0; i < stockAReturns.size(); i++ ) {
            stockA[i] = (double) stockAReturns.values().toArray()[i];
            stockB[i] = (double) stockBReturns.values().toArray()[i];
        }
        return covariance.covariance(stockA, stockB);

    }

    public static RealMatrix calculateCorrelation ( RealMatrix data ) {
        return new PearsonsCorrelation().computeCorrelationMatrix(data);
    }
}
