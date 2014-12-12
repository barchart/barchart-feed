package com.barchart.feed.api.model.meta.instrument;

import java.util.Collection;
import java.util.List;

public interface ModifiableCalendar extends Calendar {
	
	void add(Event event);
	
	void addAll(Collection<Event> events);
	
	@Override
	List<Event> events();
	
	@Override
	Event event(Event.Type type);
	
	@Override
	boolean isNull();

}
