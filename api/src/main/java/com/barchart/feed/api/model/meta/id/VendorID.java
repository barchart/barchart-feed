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
	
	/* Commonly used IDs */
	public static final VendorID BARCHART = new VendorID("BARCHART");
	public static final VendorID BARCHART_HISTORICAL = new VendorID("BARCHART_HISTORICAL");
	public static final VendorID BARCHART_SHORT = new VendorID("BARCHART_SHORT");
	public static final VendorID CQG = new VendorID("CQG");
	public static final VendorID OEC = new VendorID("OEC");

}
