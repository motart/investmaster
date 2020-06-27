package theories;

import common.InvestmentBase;
import common.MathHelper;
import common.TooFewStocksException;
import models.Opportunity;
import models.Ticker;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;

public class Markowitz extends InvestmentBase {
    private List<Opportunity> opportunitySet;
    private List<Ticker> portfolio = new ArrayList<>();
    private RealMatrix covarianceMatrix;
    private RealMatrix averageMatrix;
    private Set<Map<String, Double>> weightCombinationsSet;
    private static final Double weightStep = 0.1;
    public Markowitz( Ticker ticker) {
        this.portfolio.add( ticker );
    }

    public void run() throws TooFewStocksException {
        if ( portfolio.size() < 2 ) {
            throw new TooFewStocksException("Markowitz requires at least two stocks to run");
        }
        RealMatrix returnsMatrix = getData();
        averageMatrix = calculateAverageMatrix();
        covarianceMatrix = calculateCovarianceMatrix( returnsMatrix );
        weightCombinationsSet = generateWeightCombinationsSet( portfolio, weightStep );
    }

    public Set<Map<String, Double>> generateWeightCombinationsSet( List<Ticker> stockList, Double weightStep ) {
        double totalWeight = 0.0;
        Map<String, Double> combination = new HashMap<>();
        Set<Map<String, Double>> result = new HashSet<>();
        generateWeightCombinationsSet(stockList, totalWeight, combination, result);
        return result;
    }

    public void generateWeightCombinationsSet( List<Ticker> stockList, double totalWeight, Map<String, Double> combination, Set<Map<String, Double>> result) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if (stockList.size() == 0 && Double.parseDouble(decimalFormat.format(totalWeight)) == 1.00) {
            result.add(combination);
        } else if ( stockList.size() > 0 && totalWeight <= 1) {
            for (Ticker stock : stockList) {
                double weight = 0.0;
                while ( weight <= 1 ) {
                    Map<String, Double> tempCombination = new HashMap<>(combination);
                    double tempTotalWeight = totalWeight + weight;
                    if (tempTotalWeight <= 1) {
                        tempCombination.put(stock.getSymbol(), Double.valueOf(decimalFormat.format(weight)));
                        List<Ticker> tempStockList = new ArrayList<>(stockList);
                        tempStockList.remove(stock);
                        generateWeightCombinationsSet( tempStockList, tempTotalWeight, tempCombination, result );
                    }
                    weight = weight + weightStep;
                }
            }
        }
    }

    private RealMatrix calculateCovarianceMatrix( RealMatrix returnsMatrix ) {
        return MathHelper.calculateCovariance( returnsMatrix );
    }

    public RealMatrix getData() {
        int i = 0;
        RealMatrix result = null;
        for ( Ticker ticker: portfolio) {
            double[] returns = MathHelper.convertTreeMapToArray(ticker.getReturnsMap());
            if ( result == null ) {
                result = MatrixUtils.createRealMatrix( returns.length, this.portfolio.size() );
            }
            result.setColumn(i++, returns);
        }
        return result;
    }

    public RealMatrix calculateAverageMatrix() {
        RealMatrix result = MatrixUtils.createRealMatrix(1, portfolio.size());
        double[] row = new double[portfolio.size()];
        int i = 0;
        for ( Ticker ticker: portfolio ) {
            row[i] = ticker.getAverageReturn();
        }
        result.setRow(0, row);
        return result;
    }


    @Override
    public InvestmentBase addTicker(Ticker ticker) {
        this.portfolio.add( ticker );
        return this;
    }
}
