/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class DictKey<V> implements Dict<V>, Comment {

	private static final AtomicInteger CREATED = new AtomicInteger(0);

	private static final ConcurrentMap<Integer, Dict<?>> ENUM_MAP = //
	new ConcurrentHashMap<Integer, Dict<?>>();

	private static final ConcurrentMap<Class<Dict<?>>, AtomicInteger> INDEX_MAP = //
	new ConcurrentHashMap<Class<Dict<?>>, AtomicInteger>();

	//

	public static final Dict<?>[] values() {

		return ENUM_MAP.values().toArray(new Dict<?>[0]);

	}

	//

	private String name;
	private String guid;

	private final int ordinal;
	private final int index;
	private final String code;
	private final String comment;

	//

	protected DictKey() {
		throw new RuntimeException("do not use");
	}

	private static class Manager extends SecurityManager {
		public Class<?>[] getStack() {
			return super.getClassContext();
		}
	}

	private final static Manager manager = new Manager();

	private final Class<?> klaz;

	private final AtomicInteger INDEX = new AtomicInteger(0);

	private DictKey(final String code, final String comment) {

		this.klaz = manager.getStack()[3];

		this.ordinal = CREATED.getAndIncrement();

		this.index = INDEX.getAndIncrement();

		this.code = code;

		this.comment = comment;

		ENUM_MAP.put(ordinal, this);

	}

	@Override
	public final String name() {

		if (name == null) {

			final Field[] fields = klaz.getDeclaredFields();

			for (final Field field : fields) {
				try {
					if (this == field.get(this)) {
						name = field.getName();
						break;
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}

			if (name == null) {
				name = "" + ordinal();
			}

		}

		return name;

	}

	@Override
	public final String guid() {

		if (guid == null) {
			guid = klaz.getName() + "#" + name();
		}

		return guid;

	}

	@Override
	public final int sequence() {
		return index;
	}

	@Override
	public final int ordinal() {
		return ordinal;
	}

	@Override
	public final boolean equals(final Object dict) {

		if (dict instanceof Dict) {

			final DictKey<?> that = (DictKey<?>) dict;

			return that.ordinal() == this.ordinal();

		}

		return false;

	}

	@Override
	public final int hashCode() {

		return ordinal();

	}

	public static final Dict<?> fromOrdinal(final int ordinal) {

		final Dict<?> dict = ENUM_MAP.get(ordinal);

		if (dict == null) {
			return UNKNOWN;
		}

		return dict;

	}

	@Override
	public final String code() {

		return code;

	}

	@Override
	public final String getComment() {

		return comment;

	}

	@Override
	public final void setComment(String comment) {
		// NOOP
	}

	protected final static <X> DictKey<X> NEW(final String comment) {
		return new DictKey<X>("", comment);
	}

	protected final static <X> DictKey<X> NEW(final String code,
			final String comment) {
		return new DictKey<X>(code, comment);
	}

	// ########################

	public static final Dict<?> UNKNOWN = NEW("default instance for non-existing entry");

}
