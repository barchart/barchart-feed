package com.barchart.feed.provider;

import com.barchart.feed.api.FeedFactory;
import com.barchart.util.loader.Producer;
import com.barchart.util.value.api.Factory;

/**
 * Static service provider.
 * <p>
 * This class is excluded from api at build time and must be available from the
 * provider at run time.
 */
public class FeedFactoryProvider implements Producer<FeedFactory> {
	
	/**
	 * Load a {@link Factory}.
	 */
	@Override
	public FeedFactory produce() {
		throw new IllegalStateException("Build-time failure.");
	}

}
