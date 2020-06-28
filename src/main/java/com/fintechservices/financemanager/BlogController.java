package com.fintechservices.financemanager;

import com.fintechservices.financemanager.common.TooFewStocksException;
import com.fintechservices.financemanager.models.Ticker;
import com.fintechservices.financemanager.theories.Markowitz;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class BlogController {

    @RequestMapping("/")
    public String index() throws TooFewStocksException {
        Ticker tesla = null;
		Ticker netflix = null;
		Ticker amazon = null;
		Ticker apple = null;
		Ticker google = null;

		try {
			google = new Ticker("GOOG");
			apple = new Ticker("AAPL");
			amazon = new Ticker("AMZN");
		} catch (InterruptedException | IOException | java.text.ParseException e) {
			e.printStackTrace();
		}

		Markowitz testRun = new Markowitz( google );
		testRun.addTicker( apple ).addTicker( amazon ).addTicker( amazon );
		testRun.run();
		System.out.println("Done!!");
        return testRun.getMaxSharpe().toString();
    }

}