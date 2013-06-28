package design;

import com.barchart.feed.api.Marketplace;
import com.barchart.feed.api.Marketplace.Builder;

/**
 * Feed factory.
 * <p>
 * Use {@link FeedFactoryLoader} to load instance from provider.
 */
public interface FeedFactory {
	
	Marketplace newFeed(String username, String password);
	
	Marketplace.Builder builder();

}
