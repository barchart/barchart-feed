package com.barchart.feed.api.util;

import com.barchart.util.value.api.Existential;

/**
 * Identifier, unique in a given scope.
 */
@Deprecated
public interface Identifier extends Comparable<Identifier>, Existential {
	
	/**
	 * Identifier string representation, unique in a given scope.
	 */
	@Override
	String toString();
	
	@Override
	int hashCode();
	
	@Override
	boolean equals(Object o);
	
	interface IdentifierFactory {
		
		Identifier id(String id);
		
	}
	
	/**
	 * All instances of Identifier should use this factory to ensure
	 * all methods are implemented correctly
	 */
	IdentifierFactory FAC = new IdentifierFactory() {

		@Override
		public Identifier id(final String id) {
			
			return new Identifier() {

				@Override
				public int compareTo(final Identifier o) {
					return id.compareTo(o.toString());
				}

				@Override
				public boolean isNull() {
					return false;
				}
				
				@Override
				public int hashCode() {
					return id.hashCode();
				}
				
				@Override
				public boolean equals(final Object o) {
					if(!(o instanceof Identifier)) {
						return false;
					}
					
					return id.equals(o.toString());
				}
				
				@Override
				public String toString() {
					return id;
				}
				
			};
			
		}
		
	};
	
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
