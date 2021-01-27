package com.fintechservices.financemanager.theories;

import com.fintechservices.financemanager.common.InvestmentBase;
import com.fintechservices.financemanager.exceptions.TooFewStocksException;
import com.fintechservices.financemanager.models.Opportunity;
import com.fintechservices.financemanager.models.Ticker;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import java.text.DecimalFormat;
import java.util.*;

import static com.fintechservices.financemanager.common.MathHelper.*;

public class Markowitz implements InvestmentBase {
    private List<Opportunity> opportunitySet;
    private Set<Ticker> portfolio = new HashSet<>();
    private RealMatrix covarianceMatrix;
    private RealMatrix averageMatrix;
    private Set<LinkedHashMap<String, Double>> weightCombinationsSet;
    private Map<Integer, String> stockIndexing;
    private static Double weightStep = 0.1;
    private static final Double monthlyRiskFreeRate = 0.00123;
    public Set<Ticker> getPortfolio() {
        return portfolio;
    }

    /**
     * Entry point to applying Markowitz theory to investment optimization
     * @throws TooFewStocksException
     */
    public void run() throws TooFewStocksException {
        if ( portfolio.size() < 2 ) {
            throw new TooFewStocksException("Markowitz requires at least two stocks to run");
        }
        RealMatrix returnsMatrix = getData();
        averageMatrix = calculateAverageMatrix();
        covarianceMatrix = calculateCovarianceMatrix( returnsMatrix );
        weightCombinationsSet = generateWeightCombinationsSet( portfolio, weightStep );
        opportunitySet = generateOpportunitySet();
        Opportunity optimalReturnPortfolio = getMaxSharpe();
    }

    public Opportunity getMaxSharpe() {
        Opportunity result = new Opportunity();
        Double maxSharpe = 0.0;
        for ( Opportunity opportunity: opportunitySet ) {
            if (opportunity.sharpeRatio > maxSharpe) {
                maxSharpe = opportunity.sharpeRatio;
                result = opportunity;
            }
        }
        return result;
    }

    /**
     * Build a list of opportunity sets with sharpe ratio, risk and return for each opportunity
     * @return
     */
    private List<Opportunity> generateOpportunitySet() {
        List<Opportunity> result = new ArrayList<>();
        for ( Map<String, Double> combination: weightCombinationsSet ) {
            Opportunity opportunity = new Opportunity();
            RealMatrix combinationMatrix = MatrixUtils.createRealMatrix(1, portfolio.size());
            combinationMatrix.setRow( 0, convertMapToArray( combination ) );
            RealMatrix portfolioReturnTempMatrix = combinationMatrix.multiply(averageMatrix.transpose());
            RealMatrix portfolioVarianceTempMatrix = combinationMatrix.multiply(covarianceMatrix).multiply(combinationMatrix.transpose());
            opportunity.weights = combination;
            opportunity.portfolioReturn = portfolioReturnTempMatrix.getEntry(0,0);
            opportunity.portfolioVariance = portfolioVarianceTempMatrix.getEntry(0, 0);
            opportunity.portfolioStDeviation = calculateSquareRoot( opportunity.portfolioVariance );
            opportunity.sharpeRatio = (opportunity.portfolioReturn - monthlyRiskFreeRate )/opportunity.portfolioStDeviation;
            result.add(opportunity);
        }
        return result;
    }

    /**
     * Build a combination set of weight made of each stock where the sum of weights equals 100%
     * @param stockSet
     * @param weightStep
     * @return
     */
    public Set<LinkedHashMap<String, Double>> generateWeightCombinationsSet( Set<Ticker> stockSet, Double weightStep ) {
        double totalWeight = 0.0;
        this.weightStep = weightStep;
        LinkedHashMap<String, Double> combination = new LinkedHashMap<>();
        Set<LinkedHashMap<String, Double>> result = new HashSet<>();
        generateWeightCombinationsSet(stockSet, totalWeight, combination, result);
        return result;
    }

    /**
     * Build a combination set of weight made of each stock where the sum of weights equals 100%
     * @param stockSet
     * @param totalWeight
     * @param combination
     * @param result
     */
    private void generateWeightCombinationsSet(Set<Ticker> stockSet, double totalWeight, LinkedHashMap<String, Double> combination, Set<LinkedHashMap<String, Double>> result) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        if (stockSet.size() == 0 && Double.parseDouble(decimalFormat.format(totalWeight)) == 1.00) {
            result.add(combination);
        } else if ( stockSet.size() > 0 && totalWeight <= 1) {
            for (Ticker stock : stockSet) {
                double weight = 0.0;
                while ( weight <= 1 ) {
                    LinkedHashMap<String, Double> tempCombination = new LinkedHashMap<>(combination);
                    double tempTotalWeight = totalWeight + weight;
                    if (tempTotalWeight <= 1) {
                        tempCombination.put(stock.getSymbol(), Double.valueOf(decimalFormat.format(weight)));
                        Set<Ticker> tempStockList = new HashSet<>(stockSet);
                        tempStockList.remove(stock);
                        generateWeightCombinationsSet( tempStockList, tempTotalWeight, tempCombination, result );
                    }
                    weight = weight + weightStep;
                }
            }
        }
    }

    private RealMatrix calculateCovarianceMatrix( RealMatrix returnsMatrix ) {
        return calculateCovariance( returnsMatrix );
    }

    /**
     * Build a returns martix for all stock in the portfolio based on stock prices.
     * @return
     */
    public RealMatrix getData() {
        int i = 0;
        RealMatrix result = null;
        for ( Ticker ticker: portfolio) {
            double[] returns = convertTreeMapToArray(ticker.getReturnsMap());
            if ( result == null ) {
                result = MatrixUtils.createRealMatrix( returns.length, this.portfolio.size() );
            }
            result.setColumn(i++, returns);
        }
        return result;
    }

    /**
     * Build a matrix from calculating average returns for each stock in the portfolio
     * @return
     */
    public RealMatrix calculateAverageMatrix() {
        RealMatrix result = MatrixUtils.createRealMatrix(1, portfolio.size());
        double[] row = new double[portfolio.size()];
        int i = 0;
        for ( Ticker ticker: portfolio ) {
            // stockIndexing.put(i, ticker.getSymbol());
            row[i++] = ticker.getAverageReturn();
        }
        result.setRow(0, row);
        return result;
    }

    /**
     * Add a stock to the portfolio to be processed.
     * @param ticker
     * @return
     */
    @Override
    public InvestmentBase addTicker(Ticker ticker) {
        if ( this.portfolio.contains(ticker) ) {
            return this;
        }
        this.portfolio.add( ticker );
        return this;
    }

    public List<Opportunity> getOpportunitySet() {
        return opportunitySet;
    }
}
