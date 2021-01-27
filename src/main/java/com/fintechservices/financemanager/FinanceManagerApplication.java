package com.fintechservices.financemanager;

import com.fintechservices.financemanager.exceptions.ApiLimitReachedException;
import com.fintechservices.financemanager.exceptions.TooFewStocksException;
import com.fintechservices.financemanager.models.Ticker;
import com.fintechservices.financemanager.theories.Markowitz;
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
		Ticker palantir = null;
		Ticker accenture = null;
		Ticker acuity = null;
		Ticker fcel = null;
		Ticker plug = null;
		Ticker fdfix = null;
		Ticker fibux = null;
		Ticker fitfx = null;
		Ticker flapx = null;
		Ticker flxsx = null;

		try {
//			fdfix = new Ticker("FDFIX");
//			fibux = new Ticker("FIBUX");
//			fitfx = new Ticker("FITFX");
//			flapx = new Ticker("FLAPX");
//			flxsx = new Ticker("FLXSX");

			 fcel = new Ticker("FCEL");
			 apple = new Ticker("AAPL");
			 plug = new Ticker("PLUG");
//			 palantir = new Ticker("PLTR");
//			 accenture = new Ticker("ACN");
//			 acuity = new Ticker("AYI");
//			 amazon = new Ticker("AMZN");
//			netflix = new Ticker("NFLX");
			tesla = new Ticker("TSLA");

		} catch (InterruptedException | IOException | ParseException | ApiLimitReachedException e) {
			e.printStackTrace();
		}

		Markowitz testRun = new Markowitz( );
		testRun.addTicker(apple).addTicker(fcel).addTicker(tesla).addTicker(plug);
		testRun.run();
		System.out.println(testRun.getMaxSharpe());
	}

}
