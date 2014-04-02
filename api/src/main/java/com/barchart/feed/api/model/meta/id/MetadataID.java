package com.barchart.feed.api.model.meta.id;

import com.barchart.feed.api.model.meta.Metadata;
import com.barchart.util.common.identifier.Identifier;

public abstract class MetadataID<V extends Identifier<String, V>> extends Identifier<String, V> {

	public MetadataID(final String id, final Class<V> clazz) {
		super(id, clazz);
	}

	public abstract Metadata.MetaType metaType();

}
