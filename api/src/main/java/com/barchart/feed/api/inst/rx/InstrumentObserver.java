package com.barchart.feed.api.inst.rx;

import java.util.List;

import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.util.Observer;

public interface InstrumentObserver extends Observer<List<Instrument>> {

}
