package com.barchart.feed.api;

/**
 * Feed factory.
 * <p>
 * Use {@link FeedFactoryLoader} to load instance from provider.
 */
public interface FeedFactory {
	
	Feed newFeed(String username, String password);
	
	Feed.Builder builder();

}
