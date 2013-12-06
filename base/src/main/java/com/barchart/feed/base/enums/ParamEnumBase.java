/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.enums;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ParamEnumBase<V, T extends ParamEnum<V, T>> implements
		ParamEnum<V, T> {

	//

	protected final int ordinal;

	@Override
	public final int ordinal() {
		return ordinal;
	}

	protected final String name;

	@Override
	public final String name() {
		return name;
	}

	protected final V defaultValue;

	@Override
	public final V value() {
		return defaultValue;
	}

	//

	protected ParamEnumBase(final ParamEnumBase<?, ?>[] values,
			final int ordinal, final V defVal) {

		this.ordinal = ordinal;

		this.name = nameOf(ordinal);

		this.defaultValue = defVal;

		values[ordinal] = this;

	}

	private static final int MODIFIERS = Modifier.PUBLIC | Modifier.STATIC
			| Modifier.FINAL;

	private static final boolean isEnumField(final Field field) {
		final int modifiers = field.getModifiers();
		Class<?> klaz = field.getDeclaringClass();
		return modifiers == MODIFIERS
				&& ParamEnumBase.class.isAssignableFrom(klaz);
	}

	protected static final <E extends ParamEnumBase<?, ?>> int countEnumFields(
			Class<E> klaz) {
		final Field[] fieldArray = klaz.getDeclaredFields();
		int count = 0;
		for (final Field field : fieldArray) {
			// System.err.println("field=" + field);
			if (isEnumField(field)) {
				count++;
			}
		}
		return count;
	}

	protected String nameOf(final int ordinal) {
		final Field[] fieldArray = this.getClass().getDeclaredFields();
		int count = 0;
		for (final Field field : fieldArray) {
			if (isEnumField(field)) {
				if (count == ordinal) {
					return field.getName();
				} else {
					count++;
				}
			}
		}
		return null;
	}

	@Override
	public final String toString() {
		return name;
	}

	@Override
	public final boolean is(final ParamEnum<?, ?> that) {
		// assuming same class loader
		return this == that;
	}

	@Override
	public final boolean isIn(final ParamEnum<?, ?>... thatArray) {
		for (final ParamEnum<?, ?> that : thatArray) {
			if (is(that)) {
				return true;
			}
		}
		return false;
	}

	// @Override
	public int compareTo(final ParamEnum<?, ?> that) {
		if (that == null) {
			throw new NullPointerException("that == null");
		}
		final ParamEnumBase<?, ?> self = this;
		final ParamEnumBase<?, ?> other = (ParamEnumBase<?, ?>) that;
		if (self.getClass() != other.getClass())
			throw new ClassCastException(
					"this and that must come from same class");
		return self.ordinal - other.ordinal;
	}

	@Override
	public int sequence() {
		return ordinal;
	}

}
