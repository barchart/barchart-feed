package com.barchart.feed.api;

import com.barchart.feed.provider.FeedFactoryProvider;
import com.barchart.util.loader.BaseLoader;
import com.barchart.util.loader.Producer;

/**
 * Static service loader.
 */
public class FeedFactoryLoader {

	static class Loader extends BaseLoader<FeedFactory> {

		@Override
		protected Class<? extends Producer<FeedFactory>> providerType() {
			return FeedFactoryProvider.class;
		}

		@Override
		protected Class<FeedFactory> serviceType() {
			return FeedFactory.class;
		}
		
	}
	
	public static FeedFactory load() throws RuntimeException {
		return new Loader().produce();
	}
	
	private FeedFactoryLoader() {
		
	}
	
}
