/**
 * Copyright (C) 2011-2013 Barchart, Inc. <http://www.barchart.com/>
 *
 * All rights reserved. Licensed under the OSI BSD License.
 *
 * http://www.opensource.org/licenses/bsd-license.php
 */
package com.barchart.feed.base.values.json;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.StdDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barchart.feed.base.values.api.SizeValue;
import com.barchart.feed.base.values.provider.ValueBuilder;

class SizeValueDes extends StdDeserializer<SizeValue> {

	static Logger log = LoggerFactory.getLogger(SizeValueDes.class);

	protected SizeValueDes(final Class<SizeValue> klaz) {
		super(klaz);
	}

	@Override
	public SizeValue deserialize(final JsonParser parser,
			final DeserializationContext context) throws IOException,
			JsonProcessingException {

		try {

			final JsonToken token = parser.getCurrentToken();
			if (token != JsonToken.VALUE_STRING) {
				log.debug("token != JsonToken.VALUE_STRING");
				return null;
			}

			final String text = parser.getText();
			if (text == null) {
				log.debug("text == null");
				return null;
			}

			final long size = Long.parseLong(text);

			return ValueBuilder.newSize(size);

		} catch (final Exception e) {

			log.debug("", e);
			return null;

		}

	}

}
