package com.barchart.feed.api.series.services;

public class NodeDescriptor {
	private String specifier;
	
	public NodeDescriptor(String specifier) {
		this.specifier = specifier;
	}
	
    public String getSpecifier() {
    	return specifier;
    }
    
    public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeDescriptor other = (NodeDescriptor) obj;
		if (specifier == null) {
			if (other.specifier != null)
				return false;
		} else if (!specifier.equals(other.specifier))
			return false;
		return true;
	}
}
