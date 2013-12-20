package com.barchart.feed.api.series.services;

import java.util.List;

public abstract class NodeDescriptor {
    /** Specifier used to identify IO nodes used for bar building operations */
    public static final String TYPE_IO = "IO";
    public static final String TYPE_ASSEMBLER = "RAW";
    
	private String specifier;
	
    
	protected NodeDescriptor(String specifier) {
		this.specifier = specifier;
	}
	
    public String getSpecifier() {
    	return specifier;
    }
    
    /**
     * Uses the supplied
     * @param subscription
     * @return
     */
    public abstract List<Processor> getProcessorChain(Subscription derivableSubscription, Subscription targetSubscription);
    
    public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!NodeDescriptor.class.isAssignableFrom(obj.getClass()))
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
