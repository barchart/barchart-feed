package com.barchart.feed.meta.service;

import org.joda.time.DateTime;
import org.openfeed.InstrumentDefinition;

import rx.Observable;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.Instrument;
import com.barchart.feed.api.model.meta.Vendor;
import com.barchart.feed.api.model.meta.id.ExchangeID;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;

/**
 * Instrument metadata query service. The query object type will frequently be a
 * String representing the ticker symbol, but implementations may choose query
 * types that allow more granularity to filter instrument results by data vendor
 * or exchange (such as VendorSymbol).
 *
 * @param <Q> The lookup query object type
 */
public interface InstrumentService {

	/**
	 * Get raw Protobuf instrument definitions by ID.
	 */
	Observable<InstrumentDefinition> definitions(InstrumentID... instruments);

	/**
	 * Get instruments by ID.
	 */
	Observable<Instrument> instruments(InstrumentID... instruments);

	/**
	 * Get data vendors by ID.
	 */
	Observable<Vendor> vendors(VendorID... vendors);

	/**
	 * Get exchange definitions by ID.
	 */
	Observable<Exchange> exchanges(ExchangeID... exchanges);

	/**
	 * Get active futures contracts by root.
	 */
	Observable<InstrumentID> contracts(VendorID vendor, String root);

	/**
	 * Get active futures contracts by root as of the given historical date.
	 */
	Observable<InstrumentID> contracts(VendorID vendor, String root, DateTime date);

	/**
	 * Get all instruments belonging to a group or list (index, etc.)
	 */
	Observable<InstrumentID> group(VendorID vendor, String group);

	/**
	 * Get all options for the specified underlier.
	 */
	Observable<InstrumentID> options(InstrumentID underlier);

	/**
	 * Get all options for the specified roots as of the given historical date.
	 */
	Observable<Instrument> options(DateTime date, String... roots);

	/**
	 * Resolve a query result of InstrumentIDs into the full Instrument definitions.
	 */
	Observable<Instrument> resolve(Observable<InstrumentID> observable);

	/**
	 * Lookup instrument IDs that match the given ticker symbols. One result
	 * will always be returned for each query term, though each query result may
	 * contain multiple instrument IDs.
	 */
	Observable<InstrumentIDResult> lookupIds(LookupSymbol... symbols);

	/**
	 * Load instruments that match the given ticker symbols. This is an
	 * extension to lookupIds() that cascade loads the instrument definitions
	 * before returning results.
	 */
	Observable<InstrumentResult> lookup(LookupSymbol... symbols);

	/**
	 * Load instruments that match the given ticker symbols. This is just a
	 * convenience method for parsing symbol strings before calling
	 * {@link #lookup(LookupSymbol...)}.
	 *
	 * Symbol format ([] = optional):
	 *
	 * <pre>
	 * [[VENDOR:]EXCHANGE:]SYMBOL
	 * </pre>
	 *
	 * If the vendor is omitted, the default vendor will be assumed.
	 *
	 * Examples:
	 *
	 * <pre>
	 * IBM
	 * XNYS:IBM
	 * EDI:XNYS:IBM
	 * </pre>
	 */
	Observable<InstrumentResult> lookup(String... symbols);

	/**
	 * Map vendor symbols to actual instrument IDs. Useful for doing reverse
	 * symbology lookups for things like third-party trading gateway messages.
	 */
	Observable<Result<String, InstrumentID>> map(VendorID vendor, String... symbols);

}