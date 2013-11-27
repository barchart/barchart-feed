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

import com.barchart.feed.base.values.api.TimeValue;
import com.barchart.feed.base.values.provider.ValueBuilder;

class TimeValueDes extends StdDeserializer<TimeValue> {

	static Logger log = LoggerFactory.getLogger(TimeValueDes.class);

	protected TimeValueDes(final Class<TimeValue> klaz) {
		super(klaz);
	}

	@Override
	public TimeValue deserialize(final JsonParser parser,
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

			final long millis = Long.parseLong(text);

			return ValueBuilder.newTime(millis);

		} catch (final Exception e) {

			log.debug("", e);
			return null;

		}

	}

}
