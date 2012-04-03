package com.ddfplus.util.example;

// probably not a good idea?

class EnumForValueMore<V extends ValueIce> extends EnumForValue<V> {

	// keep first
	private static int sequence;
	private static final int size;
	private static final EnumForValue<?>[] values;
	static {
		sequence = EnumForValue.size;
		size = countEnumFields(EnumForValueMore.class) + EnumForValue.size;
		values = new EnumForValue<?>[size]; // TODO add copy from superclass
	}

	protected EnumForValueMore(V defVal) {
		super(values, sequence++, defVal);
	}

	//

	public static final EnumForValue<ValueZen> VALUE_ZEN = //
	new EnumForValue<ValueZen>(null);

}
