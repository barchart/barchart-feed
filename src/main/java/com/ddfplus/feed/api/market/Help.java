package com.ddfplus.feed.api.market;

public class Help {

	/**
	 * 
	 * Drazen, hi;
	 * 
	 * 1) for your purposes, ddf3 market api is limited to 2 interfaces: a)
	 * MAKER /ddfplus-feed-common/src/main/java/com/ddfplus/feed/api/market/
	 * MarketMaker.java b) TAKER
	 * /ddfplus-feed-common/src/main/java/com/ddfplus/feed
	 * /api/market/MarketTaker.java
	 * 
	 * 2) MAKER is market state + event product engine; it processes incoming
	 * feed messages, updates markets values & market states, generates outgoing
	 * feed request commands, fires market state events / delivers market values
	 * to the TAKERs;
	 * 
	 * 3) TAKER is market state subscriber; it has to communicate to MAKER
	 * during own registration: which market events it wants to listen on; which
	 * market instruments it is interested in; which market value (say, book-top
	 * (bid/ask), or o-h-l-c bar) to receive on each subscribed event for each
	 * interested market;
	 * 
	 * 4) basic concept of MAKER + TAKER is outlined here:
	 * /ddfplus-feed-common/src
	 * /main/java/com/ddfplus/feed/common/market/example/MarketUseCase.java
	 * 
	 * 5) here is a working maker/taker example, independent of barchart
	 * platform:
	 * /ddfplus-feed-ddf3/src/main/java/com/ddfplus/feed/market/example
	 * /MainMarket.java
	 * 
	 * 6) this is an example of test feed widget inside the platform, which uses
	 * market maker service:
	 * /barchart-plugin-core/src/main/java/com/barchart/plugin
	 * /core/impl/feed/test/FeedTestWidget.java
	 * 
	 * 7) I keep cleaning up / documenting this stuff, please indicate areas
	 * which are not clear; or if you need to expose more features;
	 * 
	 * Thank you,
	 * 
	 * Andrei
	 * 
	 * */

}
