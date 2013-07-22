package com.barchart.feed.inst.provider;

import com.barchart.feed.api.util.Identifier;

public class IdentifierImpl implements Identifier {

	private final String id;
	
	public IdentifierImpl(final String id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}

	@Override
	public int compareTo(Identifier o) {
		return toString().compareTo(o.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean isNull() {
		return this == Identifier.NULL;
	}
	
}
