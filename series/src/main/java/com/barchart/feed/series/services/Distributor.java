package com.barchart.feed.series.services;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.barchart.feed.api.model.data.Market;
import com.barchart.feed.api.series.services.HistoricalResult;


/**
 * 1. per symbol current feed and historical feed reception
 * 2. creation of output time series
 * 3. bar creation logic
 * 
 * @author David Ray
 *
 */
public class Distributor {
	private DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
	
	public void onNextMarket(Market m) {
		System.out.println("onNextMarket: " + m.instrument().symbol());
		String symbol = m.trade().instrument().symbol();
		System.out.println(new StringBuilder(symbol).append(",").append(format.print(new DateTime(m.trade().time().millisecond()))).
			append("          ").append(format.print(new DateTime(m.trade().time().millisecond()))).append(m.trade().price().asDouble()).append(",").append(m.trade().size()));
	}
	
	public <T extends HistoricalResult> void onNextHistorical(T result) {
		System.out.println("onNextHistorical: ");
		for(String s : result.getResult()) {
			System.out.println(s);
		}
	}

}
