package com.fintechservices.financemanager;

import com.fintechservices.financemanager.common.TooFewStocksException;
import com.fintechservices.financemanager.models.Opportunity;
import com.fintechservices.financemanager.models.Ticker;
import com.fintechservices.financemanager.theories.Markowitz;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

@SpringBootApplication
public class FinanceManagerApplication {

	public static void main(String[] args) throws TooFewStocksException {
		// SpringApplication.run(FinanceManagerApplication.class, args);
		Ticker tesla = null;
		Ticker netflix = null;
		Ticker amazon = null;
		Ticker apple = null;
		Ticker google = null;
		Ticker palantir = null;
		Ticker accenture = null;
		Ticker acuity = null;
		Ticker fcel = null;
		Ticker plug = null;

		try {
			fcel = new Ticker("FCEL");
			apple = new Ticker("AAPL");
			plug = new Ticker("PLUG");
			// netflix = new Ticker("NFLX");
			tesla = new Ticker("TSLA");
			// palantir = new Ticker("PLTR");
			accenture = new Ticker("ACN");
			// acuity = new Ticker("AYI");
		} catch (InterruptedException | IOException | ParseException e) {
			e.printStackTrace();
		}

		Markowitz testRun = new Markowitz( plug );
		testRun.addTicker(fcel);//.addTicker(apple).addTicker(tesla).addTicker(plug);
		testRun.run();
		System.out.println(testRun.getMaxSharpe());
	}

}
