package com.fintechservices.financemanager;

import com.fintechservices.financemanager.common.TooFewStocksException;
import com.fintechservices.financemanager.models.Ticker;
import com.fintechservices.financemanager.theories.Markowitz;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.text.ParseException;

@SpringBootApplication
public class FinanceManagerApplication {

	public static void main(String[] args) throws TooFewStocksException {
		// SpringApplication.run(FinanceManagerApplication.class, args);
		Ticker tesla = null;
		Ticker netflix = null;
		Ticker amazon = null;
		Ticker apple = null;
		Ticker google = null;

		try {
			google = new Ticker("GOOG");
			apple = new Ticker("AAPL");
			amazon = new Ticker("AMZN");
		} catch (InterruptedException | IOException | ParseException e) {
			e.printStackTrace();
		}

		Markowitz testRun = new Markowitz( google );
		testRun.addTicker( apple ).addTicker( amazon ).addTicker( amazon );
		testRun.run();
		System.out.println("Done!!");
	}

}
