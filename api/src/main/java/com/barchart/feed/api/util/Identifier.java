package com.barchart.feed.api.util;

/**
 * Identifier, unique in a given scope.
 */
public interface Identifier extends Comparable<Identifier> {
	
	/**
	 * Identifier string representation, unique in a given scope.
	 */
	@Override
	String toString();
	
	Identifier NULL = new Identifier() {
		
		private final String nul = "0".intern();
		
		@Override
		public String toString() {
			return nul;
		}

		@Override
		public int compareTo(final Identifier o) {
			return nul.compareTo(o.toString());
		}
		
		@Override
		public int hashCode() {
			return nul.hashCode();
		}
		
		// TODO Equals
		
	};

}
