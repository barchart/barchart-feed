package com.barchart.feed.api.series.analytics;

import java.util.List;

import com.barchart.feed.api.series.service.Node;
import com.barchart.feed.api.series.service.Subscription;

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
    /**
     * Uses the supplied source and target {@link Subscription}s to build
     * an IO path of {@link Node}s through which data can flow. The data
     * will start with the characteristics of the source Subscription and 
     * produce data with the characteristics described by the target Subscription.
     *  
     * @param derivableSubscription     the source subscription describing valid data
     *                                  from which the target can be produced (derivable).
     * @param   targetSubscription      the subscription describing the goal data.
     * @return  a {@link List} of {@link Node}s which is the path of processing from source
     *          to target.
     * @throws  UnsupportedOperationException   optional for some implementations.
     */
    public <N extends Node<S>, S extends Subscription> List<Node<S>> getNodeChain(S derivableSubscription, S targetSubscription);
}
