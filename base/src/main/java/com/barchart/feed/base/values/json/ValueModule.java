/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.json;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.module.SimpleModule;

import com.barchart.feed.base.values.api.DecimalValue;
import com.barchart.feed.base.values.api.PriceValue;
import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.api.TextValue;
import com.barchart.feed.base.values.api.TimeValue;

public class ValueModule {

	public static final String COLON = ":";
	public static final String EXP = "E";
	public static final String AT = "@";

	public static Module build() {

		Version version = new Version(1, 0, 0, null);

		SimpleModule module = new SimpleModule("values", version);

		//

		Class<DecimalValue> klazDecimal = DecimalValue.class;
		module.addSerializer(new DecimalValueSer(klazDecimal));
		module.addDeserializer(klazDecimal, new DecimalValueDes(klazDecimal));

		//

		Class<PriceValue> klazPrice = PriceValue.class;
		module.addSerializer(new PriceValueSer(klazPrice));
		module.addDeserializer(klazPrice, new PriceValueDes(klazPrice));

		//

		Class<SizeValue> klazSize = SizeValue.class;
		module.addSerializer(new SizeValueSer(klazSize));
		module.addDeserializer(klazSize, new SizeValueDes(klazSize));

		//

		Class<TextValue> klazText = TextValue.class;
		module.addSerializer(new TextValueSer(klazText));
		module.addDeserializer(klazText, new TextValueDes(klazText));

		//

		Class<TimeValue> klazTime = TimeValue.class;
		module.addSerializer(new TimeValueSer(klazTime));
		module.addDeserializer(klazTime, new TimeValueDes(klazTime));

		//

		return module;

	}

}
