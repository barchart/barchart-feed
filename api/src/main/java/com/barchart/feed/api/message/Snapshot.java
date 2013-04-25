package com.barchart.feed.api.message;

import com.barchart.feed.api.data.FrameworkElement;

public interface Snapshot<M extends FrameworkElement<M>> extends Message<M> {

}
