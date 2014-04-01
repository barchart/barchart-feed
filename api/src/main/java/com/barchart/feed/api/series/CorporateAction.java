package com.barchart.feed.api.series;

import org.joda.time.DateTime;

import com.barchart.feed.api.model.meta.id.InstrumentID;

public interface CorporateAction {

	/**
	 * The instrument that this action applies to.
	 */
	InstrumentID instrument();

	/**
	 * The time this action took effect.
	 */
	DateTime timestamp();

	/**
	 * The action type.
	 */
	CorporateActionType type();

	/**
	 * A readable description for this action.
	 */
	String description();

	/**
	 * Adjust the given price based on the consequence of this corporate action.
	 * Actions should only adjust prices for bar with timestamps inside their
	 * effect range.
	 */
	Bar adjust(Bar bar);

}
