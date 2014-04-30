package com.barchart.feed.meta.instrument;

import com.barchart.feed.api.model.meta.Vendor;
import com.barchart.feed.api.model.meta.id.VendorID;

public class DefaultVendor implements Vendor {

	private final VendorID id;
	private final String name;

	public DefaultVendor(final VendorID id_, final String name_) {
		id = id_;
		name = name_;
	}

	@Override
	public VendorID id() {
		return id;
	}

	@Override
	public String name() {
		return name;
	}

}
