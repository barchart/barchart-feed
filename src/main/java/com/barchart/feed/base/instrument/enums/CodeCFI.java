/**
 * Copyright (C) 2011-2012 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.instrument.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.util.values.api.Value;

/**
 * 
 * /ddfplus-feed-common/doc-inf/cfi/ISO_10962.pdf
 * 
 * http://en.wikipedia.org/wiki/ISO_10962
 * 
 * http://www.anna-web.com/index.php/home/cfiaiso10962
 * 
 * http://www.onixs.biz/tools/fixdictionary/4.4/app_6_d.html
 * 
 * http://www.quanthouse.com/misc/docs/cficode.html
 * 
 * 
 */
public enum CodeCFI implements Value<CodeCFI> {

	/* keep most specific first; 'X' means 'any' */

	/* equity */

	EQUITY_SHARES("ESXXXX"), //
	EQUITY_OTHER("EMXXXX"), //

	EQUITY("EXXXXX"), //

	/* future */

	FUTURE_SPREAD("FMXXXX"), //

	FUTURE_COMMODITY("FCXXXX"), //
	FUTURE_FINANCIAL("FFXXXX"), //

	FUTURE("FXXXXX"), //

	/* option */

	OPTION_CALL_EUROPEAN("OCEXXX"), //
	OPTION_PUT_EUROPEAN("OPEXXX"), //
	OPTION_CALL_AMERICAN("OCAXXX"), //
	OPTION_PUT_AMERICAN("OPAXXX"), //

	OPTION_SPREAD("OMXXXX"), //
	OPTION_CALL("OCXXXX"), //
	OPTION_PUT("OPXXXX"), //

	OPTION("OXXXXX"), //

	/* other */

	CURRENCY("MRCXXX"), //

	INDEX("MRIXXX"), //

	REFER("MRXXXX"), //

	//

	FUND("MUXXXX"), //

	ASSET("MXXXXX"), //

	//

	UNKNOWN("??????"), //

	;

	private static final Logger log = LoggerFactory.getLogger(CodeCFI.class);

	public static final char ANY = 'X';
	public static final char NONE = '?';
	public static final int SIZE = 6;

	/**  */
	private final String code;

	/** full text search description */
	private final String description;

	private CodeCFI(final String code) {

		assert code.length() == SIZE;

		this.code = code;

		this.description = name().replaceAll("_", " ");

	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	private static final CodeCFI[] ENUM_VALS = values();

	private static CharSequence padAny(final CharSequence codeCFI) {

		final char[] array = new char[SIZE];

		final int sizeCFI = codeCFI.length();

		for (int index = 0; index < SIZE; index++) {

			final char alpha;
			if (index < sizeCFI) {
				alpha = codeCFI.charAt(index);
			} else {
				alpha = ANY;
			}

			array[index] = alpha;

		}

		return new String(array);
	}

	/** resolve most specific designation */
	public static CodeCFI fromCode(/* local */CharSequence codeCFI) {

		if (codeCFI == null) {
			return UNKNOWN;
		}

		if (codeCFI.length() < SIZE) {
			codeCFI = padAny(codeCFI);
		}

		for (final CodeCFI known : ENUM_VALS) {
			if (known.equalsMask(codeCFI)) {
				return known;
			}
		}

		log.debug("unknown codeCFI={}", codeCFI);

		return UNKNOWN;

	}

	private boolean equalsMask(final CharSequence codeCFI) {

		charLoop: for (int index = 0; index < SIZE; index++) {

			final char thisChar = this.code.charAt(index);

			if (thisChar == ANY) {
				// ignore
				continue charLoop;
			}

			if (thisChar == codeCFI.charAt(index)) {
				// match
				continue charLoop;
			}

			return false; // first non match

		}

		return true; // total match

	}

	/* ########################## */

	@Override
	public CodeCFI freeze() {
		return this;
	}

	@Override
	public boolean isFrozen() {
		return true;
	}

	@Override
	public boolean isNull() {
		return this == UNKNOWN;
	}

}
