package com.barchart.feed.inst.osgi;

import com.barchart.feed.inst.api.InstrumentService;
import com.barchart.osgi.factory.api.FidgetManagerBase;

public class InstrumentServiceManagerProvider extends FidgetManagerBase<InstrumentService> 
	implements InstrumentServiceManager {

	@Override
	protected Class<InstrumentService> getFidgetInterface() {
		return InstrumentService.class;
	}

}
