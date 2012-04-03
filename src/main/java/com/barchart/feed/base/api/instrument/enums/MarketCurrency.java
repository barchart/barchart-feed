/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.api.instrument.enums;

import com.barchart.util.values.api.Value;

/** https://en.wikipedia.org/wiki/ISO_4217  */
public enum MarketCurrency implements Value<MarketCurrency> {

	NULL_CURRENCY, //

	ADE, 	// United Arab Emirates
	AFN, 	// Afghanistan
	ALL,	// Albania
	AMD, 	// Armenia
	AND,	// Curacto, Sint Maarten
	AOA, 	// Angola
	ARS, 	// Argentina
	AUD,	// Australia
	AWG, 	// Aruba
	AZN, 	// Azerbaijan
	BAM,	// Bosnia and Herzegovina
	BBD,	// Barbados
	BDT,	// Bangladesh
	BGN,	// Bulgaria
	BHD, 	// Bahrain
	BIF, 	// Burundi
	BMD,	// Bermuda
	BND,	// Brunei, Singapore
	BOB,	//
	BOV,	//
	BRL,	//
	BSD,	//
	BTN,	//
	BWP,	//
	BYR,	//
	BZD,	//
	CAD,	//
	CDF,	//
	CHE,	//
	CHF,	//
	CHW,	//
	CLF,	//
	CLP,	//
	CNY,	//
	COP,	//
	COU,	//
	CRC,	//
	CUC,	//
	CUP,	//
	CVE,	//
	CZK,	//
	DJF,	//
	DKK,	//
	DOP,	//
	DZD,	//
	EGP,	//
	ERN,	//
	ETB,	//
	EUR,	//
	FJD,	//
	FKP,	//
	GBP,	//
	GEL,	//
	GHS,	//
	GIP,	//
	GMD,	//
	GNF,	//
	GTQ,	//
	GYD,	//
	HKD,	//
	HNL,	//
	HRK,	//
	HTG,	//
	HUF,	//
	IDR,	//
	ILS,	//
	INR,	//
	IQD,	//
	IRR,	//
	ISK,	//
	JMD,	//
	JOD,	//
	JPY,	//
	KES,	//
	KGS,	//
	KHR,	//
	KMF,	//
	KPW,	//
	KRW,	//
	KWD,	//
	KYD,	//
	KZT,	//
	LAK,	//
	LBP,	//
	LKR,	//
	LRD,	//
	LSL,	//
	LTL,	//
	LVL,	//
	LYD,	//
	MAD,	//
	MDL,	//
	MGA,	//
	MKD,	//
	MMK,	//
	MNT,	//
	MOP,	//
	MRO,	//
	MUR,	//
	MVR,	//
	MWK,	//
	MXN,	//
	MXV,	//
	MYR,	//
	MZN,	//
	NAD,	//
	NGN,	//
	NIO,	//
	NOK,	//
	NPR,	//
	NZD,	//
	OMR,	//
	PAB,	//
	PEN,	//
	PGK,	//
	PHP,	//
	PKR,	//
	PLN,	//
	PYG,	//
	QAR,	//
	RON,	//
	RSD,	//
	RUB,	//
	RWF,	//
	SAR,	//
	SBD,	//
	SCR,	//
	SDG,	//
	SEK,	//
	SGD,	//
	SHP,	//
	SLL,	//
	SOS,	//
	SRD,	//
	SSP,	//
	STD,	//
	SYP,	//
	SZL,	//
	THB,	//
	TJS,	//
	TMT,	//
	TND,	//
	TOP,	//
	TRY,	//
	TTD,	//
	TWD,	//
	TZS,	//
	UAH,	//
	UGX,	//
	USD, 	//	
	USN,	//
	USS,	//
	UYI,	//
	UYU,	//
	UZS,	//
	VEF,	//
	VND,	//
	VUV,	//
	WST,	//
	XAF,	//
	XAG,	//
	XAU,	//
	XBA,	//
	XBB,	//
	XBC,	//
	XBD,	//
	XCD,	//
	XDR,	//
	XFU,	//
	XOF,	//
	XPD,	//
	XPF,	//
	XPT,	//
	XTS,	//
	XXX,	//
	YER,	//
	ZAR,	//
	ZMK,	//
	ZWL, 	//
	
	;

	private MarketCurrency() {

	}

	@Override
	public MarketCurrency freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == NULL_CURRENCY;
	}

	@Override
	public String toString() {
		return String.format("Currency > %4s", name());
	}

}
