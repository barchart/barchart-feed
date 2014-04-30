package com.barchart.feed.meta.service;

import java.util.List;

import com.barchart.feed.api.model.meta.Instrument;

public interface InstrumentResult extends Result<LookupSymbol, List<Instrument>> {
}