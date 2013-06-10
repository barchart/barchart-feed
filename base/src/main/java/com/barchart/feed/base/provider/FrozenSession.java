package com.barchart.feed.base.provider;

import com.barchart.feed.api.data.Session;
import com.barchart.feed.api.data.SessionData;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.Time;

class FrozenSession implements Session {
	
	private final boolean isSettled;
	private final SessionData current;
	private final SessionData currentExtended;
	private final SessionData previous;
	private final SessionData previousExtended;
	
	FrozenSession(final boolean isSettled, final SessionData current, 
			final SessionData currentExtended,
			final SessionData previous, 
			final SessionData previousExtended) {
		this.isSettled = isSettled;
		this.current = current;
		this.currentExtended = currentExtended;
		this.previous = previous;
		this.previousExtended = previousExtended;
	}

	@Override
	public boolean isSettled() {
		return isSettled;
	}
	
	@Override
	public SessionData extended() {
		return currentExtended;
	}

	@Override
	public SessionData previous() {
		return previous;
	}

	@Override
	public SessionData previousExtended() {
		return previousExtended;
	}

	@Override
	public Time lastUpdateTime() {
		return null;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public Session copy() {
		return this;
	}

	@Override
	public Price open() {
		return current.open();
	}

	@Override
	public Price high() {
		return current.high();
	}

	@Override
	public Price low() {
		return current.low();
	}

	@Override
	public Price close() {
		return current.close();
	}

	@Override
	public Price settle() {
		return current.settle();
	}

	@Override
	public Size volume() {
		return current.volume();
	}

	@Override
	public Size interest() {
		return current.interest();
	}

	@Override
	public Time timeOpened() {
		return current.timeOpened();
	}

	@Override
	public Time timeUpdated() {
		return current.timeUpdated();
	}

	@Override
	public Time timeClosed() {
		return current.timeClosed();
	}
	
}
