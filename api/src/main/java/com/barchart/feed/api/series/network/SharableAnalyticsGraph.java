package com.barchart.feed.api.series.network;

/**
 * Graph of {@link Node}s which refine one or more inputs to produce one 
 * or more outputs which can be dynamically connected to new {@link Node}s
 * which can share the previous Node's output to produce sharable output 
 * of their own. 
 * 
 * These graphs provide a mechanism for identifying the required
 * Nodes by their inputs and output such that a lookup and attachment of subsequent
 * Nodes becomes possible.
 * 
 * Nodes in this graph also ascribe to the idioms of reactive functional programming
 * as determined by the mechanisms outlined by the {@link rx.Observable} and {@link rx.Observer}
 * object combination and subscription patterns.
 * 
 * @author metaware
 *
 */
public interface SharableAnalyticsGraph {

}
