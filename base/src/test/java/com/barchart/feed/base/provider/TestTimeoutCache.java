package com.barchart.feed.base.provider;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.barchart.feed.api.model.meta.id.InstrumentID;

public class TestTimeoutCache {
	
	@Test
	public void testRemove() throws Exception {
		
		final TimeoutCache<InstrumentID> cache = new TimeoutCache<InstrumentID>(2);
		
		final List<InstrumentID> ids = new ArrayList<InstrumentID>();
		
		for(int i = 0; i <10000; i++) {
			ids.add(new InstrumentID(i));
		}
		
		Thread.sleep(10000);
		
		for(final InstrumentID id : ids) {
			cache.put(id, new Runnable() {

				@Override
				public void run() {
					
				}
				
			});
		}
		
		for(final InstrumentID id : ids) {
			cache.remove(id);
		}
		
		assertEquals(0, cache.size());
		
	}
	
	@Test
	public void testTimeout() throws Exception {
		
		final TimeoutCache<InstrumentID> cache = new TimeoutCache<InstrumentID>(2);
		
		final List<InstrumentID> ids = new ArrayList<InstrumentID>();
		
		for(int i = 0; i <10000; i++) {
			ids.add(new InstrumentID(i));
		}
		
		for(final InstrumentID id : ids) {
			cache.put(id, new Runnable() {

				@Override
				public void run() {
					
				}
				
			});
		}
		
		Thread.sleep(5 * 1000);
		
		assertEquals(0, cache.size());
		
	}

}
