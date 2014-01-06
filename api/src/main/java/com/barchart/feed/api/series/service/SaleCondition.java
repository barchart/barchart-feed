package com.barchart.feed.api.series.service;


public interface SaleCondition {
	public enum NYSE_AMEX {
		
	}
	public enum NASDAQ_OTC {
		
	}
	public enum FORM_T {
		T("Pre/post Market Trade"), 
		U("Pred/Post Market Trade - Sold out of Sequence");
		
		private String description;
		
		private FORM_T(String desc) {
			this.description = desc;
		}
		/**
		 * Returns the description string of this {@link FORM_T}
		 * @return the description string of this {@link FORM_T}
		 */
		public String description() {
			return description;
		}
	}
}
