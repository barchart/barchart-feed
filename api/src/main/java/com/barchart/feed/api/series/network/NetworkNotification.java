package com.barchart.feed.api.series.network;

import com.barchart.feed.api.series.Span;

public interface NetworkNotification {
	public Span getSpan();
	public String getSpecifier();
}
