package com.barchart.feed.api.model.meta.id;

import com.barchart.util.common.identifier.Identifier;

public class VendorID extends Identifier<String, VendorID> {

	public VendorID(String id) {
		super(id, VendorID.class);
	}

	public static final VendorID NULL = new VendorID("NULL_VENDOR_ID") {
		
		@Override
		public boolean isNull() {
			return true;
		}
		
	};
	
	public static final VendorID BARCHART = new VendorID("BARCHART");
	
}
