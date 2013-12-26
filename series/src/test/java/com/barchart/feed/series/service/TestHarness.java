package com.barchart.feed.series.service;

import java.util.List;
import java.util.Map;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.series.Span;
import com.barchart.feed.api.series.analytics.Analytic;
import com.barchart.feed.api.series.service.AnalyticContainer;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Schedule;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.TimeInterval;

public class TestHarness {
    public static Instrument makeInstrument(final String symbol) {
        return new Instrument() {

            @Override
            public int compareTo(Instrument arg0) {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public boolean isNull() {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public MetaType type() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public InstrumentID id() {
                return new InstrumentID(symbol);
            }

            @Override
            public String marketGUID() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public SecurityType securityType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public BookLiquidityType liquidityType() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public BookStructureType bookStructure() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Size maxBookDepth() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String instrumentDataVendor() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String symbol() {
               return symbol;
            }

            @Override
            public String description() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String CFICode() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Exchange exchange() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public String exchangeCode() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price tickSize() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Price pointValue() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Fraction displayFraction() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public TimeInterval lifetime() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Schedule marketHours() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public long timeZoneOffset() {
                // TODO Auto-generated method stub
                return 0;
            }

            @Override
            public String timeZoneName() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public List<InstrumentID> componentLegs() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Map<VendorID, String> vendorSymbols() {
                // TODO Auto-generated method stub
                return null;
            }
            
        };
    }
    
    public static Analytic makeEmptyAnalytic() {
        return new Analytic() {
        	@Override
            public Span preProcess(Span span) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Span process(Span span) {
                // TODO Auto-generated method stub
                return null;
            }

			@Override
			public void setAnalyticContainer(AnalyticContainer processor) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public List<String> getInputKeys() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public List<String> getOutputKeys() {
				// TODO Auto-generated method stub
				return null;
			}
		};
    }
}
