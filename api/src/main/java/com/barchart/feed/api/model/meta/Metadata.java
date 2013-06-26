package com.barchart.feed.api.model.meta;

import com.barchart.util.value.api.Existential;

/**
 * Top level interface for metadata objects.  Current extensions are instrument
 * and exchange.  Future extensions are channel and group.
 * 
 * @author Gavin M Litchfield
 *
 */
public interface Metadata extends Existential {
	
	@Override
	boolean isNull();

}
