package com.barchart.feed.series.network;

import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.network.NetworkNotification;

public class NetworkNotificationImpl implements NetworkNotification {
    private Span span;
    private String specifier;
    
    public NetworkNotificationImpl(String specifier, Span span) {
        this.span = span;
        this.specifier = specifier;
    }
    
    @Override
    public Span getSpan() {
        return span;
    }

    @Override
    public String getSpecifier() {
        return specifier;
    }

}
