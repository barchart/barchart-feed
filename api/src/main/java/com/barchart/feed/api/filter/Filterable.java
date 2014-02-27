package com.barchart.feed.api.filter;

import com.barchart.util.common.anno.aQute.bnd.annotation.ProviderType;

/**
 * Instance can participate in filtering algebra.
 * <p>
 * Supported semantics:
 * 
 * <pre>
 * semantics == ( include AND (NOT exclude) )
 * include/exclude == (term OR term OR term ... )
 * term == ( key EQUALS value )
 * </pre>
 * 
 * Permitted filter syntax:
 * 
 * <pre>
 * &((|(key=value)(key=value)(...))!(|(key=value)(key=value)(...)))
 * </pre>
 * 
 * Implicit filter scope:
 * 
 * <pre>
 * available > exchange > channel > group > instrument.
 * </pre>
 */
@ProviderType
public interface Filterable {
	
	/**
	 * Supported subset of LDAP filter operators.
	 */
	enum Operator {
		AND("&"), //
		OR("|"), //
		EQUALS("="), //
		;
		public final String code;

		Operator(final String code) {
			this.code = code;
		}
	}

	/**
	 * Logical filter branch.
	 * <p>
	 * Represents a meta list: (|(key=value)(key=value)(...))
	 */
	enum MetaStem {

		/**
		 * Include meta term into the filter.
		 */
		INCLUDE, //

		/**
		 * Exclude meta term from the filter.
		 */
		EXCLUDE, //

	}
	
}
