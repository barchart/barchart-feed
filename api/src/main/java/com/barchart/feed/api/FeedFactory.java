package com.barchart.feed.api;

/**
 * Feed factory.
 * <p>
 * Use {@link FeedFactoryLoader} to load instance from provider.
 */
public interface FeedFactory {
	
	Marketplace newFeed(String username, String password);
	
	Marketplace.Builder builder();

}
