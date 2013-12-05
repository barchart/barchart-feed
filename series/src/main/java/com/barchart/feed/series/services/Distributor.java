package com.barchart.feed.series.services;

import rx.Observer;

import com.barchart.feed.series.services.BarchartSeriesProvider.Pair;


/**
 * 1. per symbol current feed and historical feed reception
 * 2. creation of output time series
 * 3. bar creation logic
 * 
 * @author David Ray
 *
 */
public class Distributor implements Observer<Pair<String, Object>> {

	@Override
	public void onCompleted() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(Throwable e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onNext(Pair<String, Object> args) {
		System.out.println(args.first);
	}

}
