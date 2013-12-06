/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums;

/** type safe ENUM key */
public interface Dict<V> extends EnumJDK {

	/**
	 * @return creation order; can change from one JVM run another; otherwise is
	 *         GUID during JVM run session;
	 */
	int sequence();

	/**
	 * @return relative index inside class (and super class and *); immutable;
	 *         based on ENUM creation order inside inheritance hierarchy; based
	 *         on ENUM order inside each level;
	 * */
	@Override
	int ordinal();

	/**
	 * @return field name; unique inside each inheritance hierarchy
	 **/
	@Override
	String name();

	//

	/**
	 * @return immutable code for external serialization; user provided optional
	 *         parameter; not checked for uniqueness;
	 */
	String code();

	/** @return unique key based on class names space */
	String guid();

	/** equality is based on ordinal */
	@Override
	boolean equals(Object dict);

	/** hash code is based on ordinal */
	@Override
	int hashCode();

}
