package theories;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.InvestmentBase;
import models.Ticker;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static common.Constants.*;

public class Markowitz extends InvestmentBase {
    private List<Ticker> portfolio = new ArrayList<>();
    public Markowitz( Ticker ticker_1) {
        this.portfolio.add( ticker_1 );
    }

    // TODO Make this specific to the stock
    public String getData() {
        HttpResponse<String> response = null;
        for ( Ticker ticker: portfolio) {
            String path = "https://www.alphavantage.co/" + WHERE + FREQUENCY_IS + "TIME_SERIES_WEEKLY"
                    + AND + TICKER_IS + ticker.getSymbol() + AND + "apikey=CSZNI68WV3F09QQZ";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(path))
                    .build();
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return response.body();
    }

    @Override
    public InvestmentBase addTicker(Ticker ticker) {
        this.portfolio.add( ticker );
        return this;
    }
    public String getData( Ticker ticker ) {
        return null;
    }

        public TreeMap<Date, Double> getPrices(Ticker ticker) throws IOException, ParseException {
        return getPrices(getData(ticker));
    }


        // TODO Make this specific to the stock
        // TODO Add string validation
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
        return new TreeMap<Date, Double>(processedPrices);
    }

    /**
     * Calculate returns from a tree map composed of non-null keys (Dates) and non-null values (Prices)
     * @param rawData
     * @return returns
     */
    public TreeMap<Date, Float> calculateReturns( TreeMap<Date,Double> rawData ) {
        Double previousValue = null;
        Date previousDate = null;
        TreeMap<Date, Float> processedData = new TreeMap<>();
        for ( Map.Entry<Date, Double> entry: rawData.entrySet() ) {
            if ( previousValue == null && previousDate == null ) {
                previousDate = entry.getKey();
                previousValue = entry.getValue();
                continue;
            }
            processedData.put(previousDate, (float) ((entry.getValue() - previousValue) / previousValue));
            previousDate = entry.getKey();
            previousValue = entry.getValue();
        }
        return processedData;
    }


    public Double getStandardDeviation( TreeMap<Date, Double> returns ) {
        double [] testData = new double[ returns.size() ];
        for (int i=0; i < returns.size(); i++ ) {
            testData[i] = (double) returns.values().toArray()[i];
        }
        DescriptiveStatistics statistics = new DescriptiveStatistics( testData );
        return statistics.getStandardDeviation();
    }

    public Double getVariance( TreeMap<Date, Double> returns ) {
        double [] testData = new double[ returns.size() ];
        for (int i=0; i < returns.size(); i++ ) {
            testData[i] = (double) returns.values().toArray()[i];
        }
        DescriptiveStatistics statistics = new DescriptiveStatistics( testData );
        return statistics.getVariance();
    }

    public Double getAverageReturn( TreeMap<Date, Float> returns ) {
        Double result;
        result = returns.values().stream().mapToDouble(num -> Double.valueOf(num)).average().orElse(0);
        return result;
    }

}
