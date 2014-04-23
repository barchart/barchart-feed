package com.barchart.feed.api.model.meta;

import com.barchart.feed.api.model.meta.id.VendorID;

/**
 * A market data vendor. Each vendor has a unique set of instruments, exchanges
 * and symbology for their data set.
 */
public interface Vendor {

	public static final Vendor DEFAULT = new Vendor() {

		private final VendorID id = VendorID.BARCHART;
		private final String name = "Barchart";

		@Override
		public VendorID id() {
			return id;
		}

		@Override
		public String name() {
			return name;
		}

	};

	/**
	 * The unique vendor ID.
	 */
	VendorID id();

	/**
	 * A descriptive name for the data vendor.
	 */
	String name();

}
