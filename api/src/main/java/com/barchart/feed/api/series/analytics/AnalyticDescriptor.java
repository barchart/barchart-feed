package com.barchart.feed.api.series.analytics;

import com.barchart.feed.api.series.service.NodeDescriptor;

/**
 * Further extension of {@link NodeDescriptor} to emphasize the needed
 * {@link Analytic} functionality. 
 * 
 * @author metaware
 */
public interface AnalyticDescriptor extends NodeDescriptor {
    /**
     * Returns the {@link Analytic}'s class.
     * @return  the analytic's class.
     */
    public Class<? extends Analytic> getAnalyticClass();
    /**
     * Sets the {@link Analytic}'s class.
     * @param clazz the {@link Analytic}'s class.
     */
    public void setAnalyticClass(Class<? extends Analytic> clazz);
    /**
     * Returns the underlying {@link Analytic}'s constructor
     * arguments.
     * @return  the underlying {@link Analytic}'s constructor arguments.
     */
    public int[] getConstructorArgs();
    /**
     * Sets the underlying {@link Analytic}'s constructor
     * arguments.
     * @param    args    the underlying {@link Analytic}'s constructor arguments.
     */
    public void setConstructorArgs(int[] args);
    /**
     * Returns the Analytic class subtype specified by this descriptor's
     * configuration.
     * 
     * @return  the Analytic class subtype
     */
    public Analytic instantiateAnalytic();
    
}
