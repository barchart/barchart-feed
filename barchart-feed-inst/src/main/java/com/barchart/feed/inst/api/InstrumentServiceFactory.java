package com.barchart.feed.inst.api;

import java.util.List;
import java.util.concurrent.FutureTask;

public final class InstrumentServiceFactory {
	
	private InstrumentServiceFactory() {
		
	}
	
	public static final InstrumentService build(final SymbologyContext symbCtx, 
			final MetadataContext mtdtaCtx) {
		
		return new InstrumentService() {

			@Override
			public Instrument lookup(final CharSequence symbol) {
				
				final InstrumentGUID defaultGUID = symbCtx.lookup(symbol);
				final InstrumentDef instDef = mtdtaCtx.lookup(defaultGUID);
				
				return new Instrument() {

					@Override
					public InstrumentGUID getGUID() {
						return defaultGUID;
					}

					@Override
					public InstrumentDef getInstrumentDef() {
						return instDef;
					}
					
				};
				
			}

			@Override
			public FutureTask<Instrument> lookupAsync(final CharSequence symbol) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Instrument lookup(final InstrumentGUID guid) {
				
				final InstrumentDef instDef = mtdtaCtx.lookup(guid);
				
				return new Instrument() {

					@Override
					public InstrumentGUID getGUID() {
						return guid;
					}

					@Override
					public InstrumentDef getInstrumentDef() {
						return instDef;
					}
					
				};
			}

			@Override
			public FutureTask<Instrument> lookupAsnyc(final InstrumentGUID guid) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<Instrument> lookup(List<CharSequence> symbols) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public FutureTask<List<Instrument>> lookupAsync(
					List<CharSequence> symbols) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
	}

	
	
}
