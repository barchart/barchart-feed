package com.barchart.feed.base.provider;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.barchart.feed.base.participant.FrameworkAgent;
import com.barchart.feed.base.sub.Sub;
import com.barchart.feed.base.sub.Sub.Type;
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
	
	public Sub subscribe(final FrameworkAgent<?> agent, final String interest) {
		
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
			return new SubscriptionBase(interest, Type.INSTRUMENT, stuffToAdd);
		} else {
			return Sub.NULL;
		}
		
	}

	public Set<Sub> subscribe(final FrameworkAgent<?> agent, 
			final Set<String> interests) {
		
		final Set<Sub> newSubs = new HashSet<Sub>();
		
		for(final String interest : interests) {
			final Sub sub = subscribe(agent, interest);
			if(!sub.isNull()) {
				newSubs.add(sub);
			}
		}
		
		return newSubs;
		
	}

	public Sub unsubscribe(final FrameworkAgent<?> agent, 
			final String interest) {
		
		if(!agentMap.containsKey(agent)) {
			return Sub.NULL;
		}
		
		final Set<SubscriptionType> oldSubs = agentMap.remove(agent);
		
		subs.get(interest).remove(oldSubs);
		
		if(subs.get(interest).isEmpty()) {
			subs.remove(interest);
		}
		
		final Set<SubscriptionType> stuffToRemove = EnumSet.copyOf(oldSubs);
		stuffToRemove.removeAll(aggregate(interest));
		
		if(!stuffToRemove.isEmpty()) {
			return new SubscriptionBase(interest, Type.INSTRUMENT, stuffToRemove);
		} else {
			return Sub.NULL;
		}
		
	}

	public Set<Sub> unsubscribe(final FrameworkAgent<?> agent, 
			final Set<String> interests) {
		
		final Set<Sub> newSubs = new HashSet<Sub>();
		
		for(final String interest : interests) {
			final Sub sub = unsubscribe(agent, interest);
			if(!sub.isNull()) {
				newSubs.add(sub);
			}
		}
		
		return newSubs;
		
	}

}
