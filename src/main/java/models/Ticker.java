package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.MathHelper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static common.Constants.*;

public class Ticker {
    private String symbol;
    private Double averageReturn;
    private Double variance;
    private Double standardDeviation;
    private TreeMap<Date, Double> pricesMap;
    private TreeMap<Date, Double> returnsMap;

    public Ticker( String symbol) throws IOException, ParseException, InterruptedException {
        this.setSymbol(symbol);
        retrieveData( symbol );
    }

    private void retrieveData ( String symbol ) throws IOException, ParseException, InterruptedException {
        String rawData = getData( symbol );
        pricesMap = getPrices( rawData );
        setReturnsMap(MathHelper.calculateReturns( pricesMap));
        averageReturn = MathHelper.calculateAverageReturn(getReturnsMap());
        variance = MathHelper.calculateVariance(getReturnsMap());
    }

    private String getData( String symbol ) throws IOException, InterruptedException {
            HttpResponse<String> response;
            String path = "https://www.alphavantage.co/" + WHERE + FREQUENCY_IS + "TIME_SERIES_WEEKLY"
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

    public TreeMap<Date, Double> getPrices(String rawDataString) throws IOException, ParseException {
        HashMap<Date,Double> processedPrices = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(rawDataString);
        Iterator<JsonNode> elements = rootNode.elements();
        elements.next(); // Skip metadata
        JsonNode data = elements.next(); //
        Iterator<Map.Entry<String, JsonNode>> datedPrices = data.fields();
        while (datedPrices.hasNext()) {
            Map.Entry<String, JsonNode> test = datedPrices.next();
            processedPrices.put(new SimpleDateFormat("yyyy-MM-dd").parse(test.getKey()) , test.getValue().get("4. close").asDouble());
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

    public TreeMap<Date, Double> getReturnsMap() {
        return returnsMap;
    }

    public void setReturnsMap(TreeMap<Date, Double> returnsMap) {
        this.returnsMap = returnsMap;
    }
}
