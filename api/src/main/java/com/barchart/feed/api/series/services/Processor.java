package com.barchart.feed.api.series.services;

/**
 * Base type for all {@link Analytic}s, {@link Assembler}s, and {@link BarBuilder}s.
 *  
 * @author David Ray
 *
 */
public interface Processor {
    public enum Category { ASSEMBLER, BAR_BUILDER, ANALYTIC };
    
    /**
     * Returns the category nature of this {@code Processor}
     *  
     * @return  the category nature of this {@code Processor}
     */
    public Category getCategory();
    /**
     * Adds an input {@link Subscription} mapped to the specified key.
     * 
     * @param subscription
     * @param key
     */
    public void addInputSubscription(String key, Subscription subscription);
    /**
     * Adds an output {@link Subscription} mapped to the specified key.
     * 
     * @param subscription
     * @param key
     */
    public void addOutputSubscription(String key, Subscription subscription);
    /**
     * Returns the input {@link Subscription} mapped to the specified key.
     * @param key  the mapping for the input Subscription
     * @return the Subscription corresponding to the specified key.
     */
    public Subscription getInputSubscription(String key);
    /**
     * Returns the {@link Subscription} corresponding to the specified key;
     * 
     * @param      key     the key mapped to the required output
     * @return             the required output
     */
    public Subscription getOutputSubscription(String key);
    
}
