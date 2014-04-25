package com.barchart.feed.meta.service;

import com.barchart.feed.api.model.meta.id.ExchangeID;
import com.barchart.feed.api.model.meta.id.VendorID;

public interface LookupSymbol {

	VendorID vendor();

	ExchangeID exchange();

	String symbol();

}
