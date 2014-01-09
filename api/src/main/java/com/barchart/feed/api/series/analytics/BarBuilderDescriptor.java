package com.barchart.feed.api.series.analytics;


public interface BarBuilderDescriptor extends AnalyticDescriptor {
    /**
     * Returns the underlying {@link Analytic}'s constructor
     * argument.
     * @return  the underlying {@link Analytic}'s constructor arguments.
     */
    public <T extends Subscription> T getConstructorArg();
    /**
     * Sets the underlying {@link Analytic}'s constructor
     * argument.
     * @param    arg    the underlying {@link Analytic}'s constructor arguments.
     */
    public <T extends Subscription> void setConstructorArg(T arg);
    /**
     * Instantiates the sub type of the {@link Analytic} class specified.
     * 
     * @return      the instantiated Analytic
     */
    public Analytic instantiateBuilderAnalytic();
    
}
