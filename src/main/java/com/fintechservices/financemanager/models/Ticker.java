package com.fintechservices.financemanager.models;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fintechservices.financemanager.exceptions.ApiLimitReachedException;
import com.fintechservices.financemanager.common.MathHelper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static com.fintechservices.financemanager.common.Constants.*;

public class Ticker {
    private String symbol;
    private Double averageReturn;
    private Double variance;
    private Double standardDeviation;
    private TreeMap<LocalDate, Double> pricesMap;
    private TreeMap<LocalDate, Double> returnsMap;
    private LocalDate startDate = LocalDate.now().minusMonths(1);
    private LocalDate endDate = LocalDate.now();
    // private Date endDate = new Date(System.currentTimeMillis());

    public Ticker( String symbol) throws IOException, ParseException, InterruptedException, ApiLimitReachedException {
        this.setSymbol(symbol);
        retrieveData( symbol );
    }

    private void retrieveData ( String symbol ) throws IOException, ParseException, InterruptedException, ApiLimitReachedException {
        String rawData = getData( symbol );
        pricesMap = getPrices( rawData );
        setReturnsMap(MathHelper.calculateReturns( pricesMap));
        averageReturn = MathHelper.calculateAverageReturn(getReturnsMap());
        variance = MathHelper.calculateVariance(getReturnsMap());
    }

    private String getData( String symbol ) throws IOException, InterruptedException {
            HttpResponse<String> response;
            String path =  "https://www.alphavantage.co/" + WHERE + FREQUENCY_IS + "TIME_SERIES_DAILY"
                    + AND + TICKER_IS + symbol + AND + "apikey=CSZNI68WV3F09QQZ";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(path))
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response != null) {
                return response.body();
            }
            return "";
    }

    public TreeMap<LocalDate, Double> getPrices(String rawDataString) throws IOException, ParseException, ApiLimitReachedException {
        if (rawDataString.startsWith("Thank you")) {
            throw new ApiLimitReachedException(rawDataString);
        }
        HashMap<LocalDate,Double> processedPrices = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(rawDataString);
        Iterator<JsonNode> elements = rootNode.elements();
        elements.next(); // Skip metadata
        JsonNode data = elements.next(); //
        Iterator<Map.Entry<String, JsonNode>> datedPrices = data.fields();
        while (datedPrices.hasNext()) {
            Map.Entry<String, JsonNode> test = datedPrices.next();
            LocalDate entryDate = LocalDate.parse(test.getKey());
            // Check the date here
            if (entryDate.compareTo( startDate ) >= 0 && entryDate.compareTo(endDate) <= 0 ) {
                processedPrices.put(entryDate , test.getValue().get("4. close").asDouble());
            }
        }
        return new TreeMap<>(processedPrices);
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getAverageReturn() {
        return averageReturn;
    }

    public void setAverageReturn(Double averageReturn) {
        this.averageReturn = averageReturn;
    }

    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public TreeMap<LocalDate, Double> getReturnsMap() {
        return returnsMap;
    }

    public void setReturnsMap(TreeMap<LocalDate, Double> returnsMap) {
        this.returnsMap = returnsMap;
    }

    public TreeMap<LocalDate, Double> getPricesMap() {
        return pricesMap;
    }

    /**
     * Check if the latest closing price is significantly below the moving
     * average of recent prices.
     *
     * @param lookbackDays number of historical days to consider
     * @param threshold    how many standard deviations below the average
     *                     the price must be to be considered a dip
     * @return true if the stock is in a dip
     */
    public boolean isInDip(int lookbackDays, double threshold) {
        if (pricesMap == null || pricesMap.isEmpty()) {
            return false;
        }

        List<Double> recentPrices = new ArrayList<>();
        int count = 0;
        for (Double price : pricesMap.descendingMap().values()) {
            recentPrices.add(price);
            if (++count >= lookbackDays) {
                break;
            }
        }

        if (recentPrices.size() < 2) {
            return false;
        }

        double sum = 0.0;
        for (Double p : recentPrices) {
            sum += p;
        }
        double mean = sum / recentPrices.size();

        double varianceTemp = 0.0;
        for (Double p : recentPrices) {
            varianceTemp += Math.pow(p - mean, 2);
        }
        double stDev = Math.sqrt(varianceTemp / recentPrices.size());

        double latestPrice = recentPrices.get(0);
        return latestPrice < (mean - threshold * stDev);
    }

    public boolean isInDip() {
        return isInDip(50, 1.0);
    }
}
