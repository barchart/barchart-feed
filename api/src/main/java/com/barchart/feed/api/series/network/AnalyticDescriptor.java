package com.barchart.feed.api.series.network;


/**
 * Further extension of {@link NodeDescriptor} to emphasize the needed
 * {@link Analytic} functionality. 
 * 
 * @author David Ray
 */
public interface AnalyticDescriptor extends NodeDescriptor {
	/**
     * Returns the output key used by the underlying {@link Analytic}
     * @return  the output key
     */
    public String getOutputKey();
    /**
     * Sets the output key used by the underlying {@link Analytic}
     * @param  key     the output key
     */
    public void setOutputKey(String key);
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
