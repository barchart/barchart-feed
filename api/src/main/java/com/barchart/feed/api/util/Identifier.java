package com.barchart.feed.api.util;

import com.barchart.util.value.api.Existential;

/**
 * Identifier, unique in a given scope.
 */
public interface Identifier extends Comparable<Identifier>, Existential {
	
	/**
	 * Identifier string representation, unique in a given scope.
	 */
	@Override
	String toString();
	
	Identifier NULL = new Identifier() {
		
		private final String nul = "NULL ID".intern();
		
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
		
		@Override
		public boolean equals(final Object o) {
			
			if(!(o instanceof Identifier)) {
				return false;
			}
			
			return nul.equals(((Identifier)o).toString());
			
		}
		
		@Override
		public boolean isNull() {
			return true;
		}
		
	};

}
