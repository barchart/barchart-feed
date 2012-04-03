package com.ddfplus.feed.common.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.barchart.util.anno.NotThreadSafe;
import com.barchart.util.anno.ThreadSafe;
import com.ddfplus.feed.common.util.concurrent.Runner;
import com.ddfplus.feed.common.util.concurrent.RunnerLoop;

@NotThreadSafe
public class FastEnumMap<K extends BitSetEnum<K>, V> extends FastEnumBase<K>
		implements RunnerLoop<Entry<K, V>>, Map<K, V> {

	protected final FastEnumSet<K> enumSet = new FastEnumSet<K>(enumValues);

	@SuppressWarnings("unchecked")
	protected final V[] values = (V[]) new Object[enumValues.length];

	protected FastEnumMap(final K[] values) {
		super(values);
	}

	//

	public final long bitSet() {
		return enumSet.bitSet();
	}

	@ThreadSafe(rule = "does not reflect concurrent modifications")
	@Override
	public <R> void runLoop(final Runner<R, Entry<K, V>> mapTask,
			final List<R> list) {

		final MapEntry<K, V> entry = new MapEntry<K, V>();

		final Runner<R, K> setTask = new Runner<R, K>() {
			@Override
			public R run(final K key) {
				final V value = values[key.ordinal()];
				if (value == null) {
					// due to concurrent modifications
					return null;
				} else {
					entry.key = key;
					entry.value = value;
					return mapTask.run(entry);
				}
			}
		};

		enumSet.runLoop(setTask, list);

	}

	@Override
	public final boolean isEmpty() {
		return enumSet.isEmpty();
	}

	@Override
	public final V get(final Object key) {
		return values[((BitSetEnum<?>) key).ordinal()];
	}

	@Override
	public final V put(final K key, final V newValue) {
		final int ordinal = key.ordinal();
		final V oldValue = values[ordinal];
		values[ordinal] = newValue;
		enumSet.add(key);
		return oldValue;

	}

	@Override
	public final V remove(final Object key) {
		final int ordinal = ((BitSetEnum<?>) key).ordinal();
		final V oldValue = values[ordinal];
		values[ordinal] = null;
		enumSet.remove(key);
		return oldValue;
	}

	protected final Runner<Void, K> taskClear = //
	new Runner<Void, K>() {
		@Override
		public Void run(final K key) {
			values[key.ordinal()] = null;
			return null;
		}
	};

	@Override
	public final void clear() {
		enumSet.runLoop(taskClear, null);
		enumSet.clear();
	}

	@Override
	public int size() {
		return enumSet.size();
	}

	protected final Runner<V, K> taskGet = //
	new Runner<V, K>() {
		@Override
		public V run(final K key) {
			return values[key.ordinal()];
		}
	};

	@Override
	public Collection<V> values() {
		final List<V> list = new ArrayList<V>(size());
		enumSet.runLoop(taskGet, list);
		return list;
	}

	@Override
	public Set<K> keySet() {
		return enumSet;
	}

	@Override
	public boolean containsKey(final Object key) {
		return enumSet.contains(key);
	}

	// #####################################################################

	@Override
	public boolean containsValue(final Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	// #####################################################################

}
