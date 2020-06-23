package theories;

import common.InvestmentInterface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static common.Constants.*;

public class Markowitz implements InvestmentInterface {
    private String path = "https://www.alphavantage.co/" + WHERE + FREQUENCY_IS + "TIME_SERIES_WEEKLY"
            + AND + TICKER_IS + "IBM" + AND + "apikey=CSZNI68WV3F09QQZ";
    public String getData() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(path))
                .build();

        HttpResponse<String> response =
                null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return response.body();
    }

    public void calculateReturns() {

    }

}
