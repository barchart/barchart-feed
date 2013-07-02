package com.barchart.feed.api.filter;

import aQute.bnd.annotation.ProviderType;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.util.anno.NotMutable;

/**
 * Market data content request filter expression.
 * <p>
 * Constructed with supported {@link Filterable} semantics.
 * <p>
 * Used in both server and client.
 * 
 * @see Filterable
 */
@NotMutable
@ProviderType
public interface Filter {
	
	/**
	 * Filter builder.
	 * <p>
	 * Filter resolution process will create filter in a canonical form, i.e. no
	 * duplicates, no contradictions.
	 */
	@ProviderType
	interface Builder {
		
		/** Request all markets available for the account. */
		Builder available();

		//

		/** Include all markets by exchange code. */
		Builder exchangeInclude(String... code);

		/** Exclude all markets by exchange code. */
		Builder exchangeExclude(String... code);

		/** Include all markets by feed-channel code. */
		Builder channelInclude(String... code);

		/** Exclude all markets by feed-channel code. */
		Builder channelExclude(String... code);

		/** Include all markets by symbol-group code. */
		Builder groupInclude(String... code);

		/** Exclude all markets by symbol-group code. */
		Builder groupExclude(String... code);

		/** Include markets by symbol code. */
		Builder instrumentInclude(String... code);

		/** Exclude markets by symbol code. */
		Builder instrumentExclude(String... code);
		
		//

		/** Include any previously resolved market entity. */
		Builder metaInclude(Metadata... meta);

		/** Exclude any previously resolved market entity. */
		Builder metaExclude(Metadata... meta);

		//
		
		/**
		 * Resolve names into {@link Metadata}, convert to canonical form.
		 */
		Filter build();
		
	}
	
	/**
	 * Verify that market matches the filter.
	 */
	boolean hasMatch(Instrument instrument);

	/**
	 * Resolved filter in the LDAP format http://www.ietf.org/rfc/rfc1960.txt.
	 */
	String expression();
	
}
