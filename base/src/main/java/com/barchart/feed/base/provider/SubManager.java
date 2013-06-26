package com.barchart.feed.base.provider;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.barchart.feed.api.FrameworkAgent;
import com.barchart.feed.base.sub.Subscription;
import com.barchart.feed.base.sub.SubscriptionType;

/*
 * This was a work up class, currently broken down inside MarketplaceBase
 */
public class SubManager {

	private final Map<String, Set<Set<SubscriptionType>>> subs = 
			new HashMap<String, Set<Set<SubscriptionType>>>();
	
	private final Map<FrameworkAgent<?>, Set<SubscriptionType>> agentMap = 
			new HashMap<FrameworkAgent<?>, Set<SubscriptionType>>();
	
	private Set<SubscriptionType> aggregate(final String interest) {
		
		final Set<SubscriptionType> agg = EnumSet.noneOf(SubscriptionType.class);
		
		for(final Set<SubscriptionType> set : subs.get(interest)) {
			agg.addAll(set);
		}
		
		return agg;
	}
	
	public Subscription subscribe(final FrameworkAgent<?> agent, final String interest) {
		
		if(!agentMap.containsKey(agent)) {
			agentMap.put(agent, SubscriptionType.mapMarketEvent(agent.type()));
		}
		
		final Set<SubscriptionType> newSubs = agentMap.get(agent);
		
		if(!subs.containsKey(interest) && !newSubs.isEmpty()) {
			subs.put(interest, new HashSet<Set<SubscriptionType>>());
		}
		
		// Compare newSubs to aggregate(interest) 
		final Set<SubscriptionType> stuffToAdd = EnumSet.copyOf(newSubs);
		stuffToAdd.removeAll(aggregate(interest));
		
		if(!stuffToAdd.isEmpty()) {
			return new SubscriptionBase(interest, stuffToAdd);
		} else {
			return Subscription.NULL_SUBSCRIPTION;
		}
		
	}

	public Set<Subscription> subscribe(final FrameworkAgent<?> agent, 
			final Set<String> interests) {
		
		final Set<Subscription> newSubs = new HashSet<Subscription>();
		
		for(final String interest : interests) {
			final Subscription sub = subscribe(agent, interest);
			if(!sub.isNull()) {
				newSubs.add(sub);
			}
		}
		
		return newSubs;
		
	}

	public Subscription unsubscribe(final FrameworkAgent<?> agent, 
			final String interest) {
		
		if(!agentMap.containsKey(agent)) {
			return Subscription.NULL_SUBSCRIPTION;
		}
		
		final Set<SubscriptionType> oldSubs = agentMap.remove(agent);
		
		subs.get(interest).remove(oldSubs);
		
		if(subs.get(interest).isEmpty()) {
			subs.remove(interest);
		}
		
		final Set<SubscriptionType> stuffToRemove = EnumSet.copyOf(oldSubs);
		stuffToRemove.removeAll(aggregate(interest));
		
		if(!stuffToRemove.isEmpty()) {
			return new SubscriptionBase(interest, stuffToRemove);
		} else {
			return Subscription.NULL_SUBSCRIPTION;
		}
		
	}

	public Set<Subscription> unsubscribe(final FrameworkAgent<?> agent, 
			final Set<String> interests) {
		
		final Set<Subscription> newSubs = new HashSet<Subscription>();
		
		for(final String interest : interests) {
			final Subscription sub = unsubscribe(agent, interest);
			if(!sub.isNull()) {
				newSubs.add(sub);
			}
		}
		
		return newSubs;
		
	}

}
