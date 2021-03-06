/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.json;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.ScalarSerializerBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.values.api.SizeValue;

class SizeValueSer extends ScalarSerializerBase<SizeValue> {

	protected SizeValueSer(final Class<SizeValue> klaz) {
		super(klaz);
	}

	static Logger log = LoggerFactory.getLogger(SizeValueSer.class);

	@Override
	public void serialize(final SizeValue value, final JsonGenerator jgen,
			final SerializerProvider provider) throws IOException,
			JsonProcessingException {

		jgen.writeString(Long.toString(value.asLong()));

	}
}
