/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * base class for type safe dictionary enumerators
 */
public class DictEnum<V> implements Dict<V>, Comment {

	{
		// System.out.println("LOAD DictEnum");
	}

	private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

	private static final ConcurrentMap<Integer, DictEnum<?>> ENUM_MAP = //
	new ConcurrentHashMap<Integer, DictEnum<?>>();

	private static final ConcurrentMap<Class<DictEnum<?>>, AtomicInteger> ORDINAL_MAP = //
	new ConcurrentHashMap<Class<DictEnum<?>>, AtomicInteger>();

	private static final ConcurrentMap<Class<DictEnum<?>>, DictEnum<?>[]> VALUES_MAP = //
	new ConcurrentHashMap<Class<DictEnum<?>>, DictEnum<?>[]>();

	//

	/** @return clone array */
	@SuppressWarnings("unchecked")
	public static final <K extends DictEnum<?>> DictEnum<?>[] valuesFor(
			final Class<K> klaz) {

		DictEnum<?>[] values = VALUES_MAP.get(klaz);

		if (values == null) {

			try {
				/* initialize klaz, assign static fields */
				final Constructor<?> ctor = klaz.getDeclaredConstructor();
				ctor.setAccessible(true);
				ctor.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}

			final int size = ENUM_MAP.size();

			final List<DictEnum<?>> target = new ArrayList<DictEnum<?>>(size);

			final Set<String> nameSet = new HashSet<String>();

			for (int k = 0; k < size; k++) {
				final DictEnum<?> dict = ENUM_MAP.get(k);
				if (dict.getClass().isAssignableFrom(klaz)) {
					target.add(dict);
					nameSet.add(dict.name());
				}
			}

			if (nameSet.size() != target.size()) {

				System.out.println("contains duplicate names : " + klaz);

				for (final DictEnum<?> dict : target) {
					System.out.println(" name=" + dict.name() + " guid="
							+ dict.guid());
				}

				throw new RuntimeException("duplicate names in dict enum");

			}

			values = target.toArray(new DictEnum<?>[0]);

			VALUES_MAP.put((Class<DictEnum<?>>) klaz, values);

		}

		return values.clone();

	}

	@SuppressWarnings("unchecked")
	protected static final <K extends DictEnum<?>> K[] valuesForType(
			final Class<K> klaz) {

		final DictEnum<?>[] source = valuesFor(klaz);

		final int size = source.length;

		final K[] target = (K[]) Array.newInstance(klaz, size);

		for (int k = 0; k < size; k++) {
			target[k] = (K) source[k];
		}

		return target;

	}

	//

	private String name;
	private String guid;

	private final int sequence;
	private final int ordinal;
	private final String code;
	private final String comment;

	//

	@SuppressWarnings("unchecked")
	private int makeOrdinal() {

		final Class<DictEnum<?>> klaz = (Class<DictEnum<?>>) getClass();

		final Class<?> parent = klaz.getSuperclass();

		// System.out.println("parent=" + parent);

		AtomicInteger parentCount = ORDINAL_MAP.get(parent);
		if (parentCount == null) {
			parentCount = new AtomicInteger(0);
			ORDINAL_MAP.put((Class<DictEnum<?>>) parent, parentCount);
		}

		// System.out.println("parentCount=" + parentCount);

		final AtomicInteger countOld = ORDINAL_MAP.get(klaz);

		if (countOld == null) {

			// initial instance

			final int indexStart;

			if (parent == DictEnum.class) {
				indexStart = 0;
			} else {
				indexStart = parentCount.get();
			}

			final AtomicInteger countNew = new AtomicInteger(indexStart);

			ORDINAL_MAP.put(klaz, countNew);

			return countNew.getAndIncrement();

		} else {

			// following instance

			return countOld.getAndIncrement();

		}
	}

	private int makeSequence() {
		return SEQUENCE.getAndIncrement();
	}

	/** use for class load only */
	protected DictEnum() {
		this.code = null;
		this.comment = null;
		this.sequence = -1;
		this.ordinal = -1;
	}

	protected DictEnum(final String comment) {
		this("", comment);
	}

	protected DictEnum(final String code, final String comment) {
		this.code = code;
		this.comment = comment;
		this.sequence = makeSequence();
		this.ordinal = makeOrdinal();
		ENUM_MAP.put(sequence, this);
	}

	@Override
	public final String name() {
		if (name == null) {
			final Field[] fields = getClass().getDeclaredFields();
			for (final Field field : fields) {
				try {
					field.setAccessible(true);
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
			guid = getClass().getName() + "#" + name();
		}
		return guid;
	}

	@Override
	public final int sequence() {
		return sequence;
	}

	@Override
	public final int ordinal() {
		return ordinal;
	}

	@Override
	public final boolean equals(final Object dict) {
		if (dict instanceof DictEnum) {
			final DictEnum<?> that = (DictEnum<?>) dict;
			return that.sequence() == this.sequence();
		}
		return false;
	}

	@Override
	public final int hashCode() {
		return sequence();
	}

	@Override
	public String code() {
		return code;
	}

	@Override
	public final String getComment() {
		return comment;
	}

	@Override
	public final void setComment(String comment) {
		new UnsupportedOperationException("do not use").printStackTrace();
	}

	private String text;

	@Override
	public String toString() {
		if (text == null) {
			text = String
					.format("NAME=%-20s ORDINAL=%02d SEQUENCE=%02d GUID=%30s  COMMENT=%s ",
							name(), ordinal(), sequence(), guid(), getComment());
		}
		return text;
	}

}
