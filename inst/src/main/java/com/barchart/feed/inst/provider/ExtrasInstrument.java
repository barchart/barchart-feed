package com.barchart.feed.inst.provider;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

import com.barchart.feed.api.model.meta.Exchange;
import com.barchart.feed.api.model.meta.id.InstrumentID;
import com.barchart.feed.api.model.meta.id.VendorID;
import com.barchart.feed.api.model.meta.instrument.Calendar;
import com.barchart.feed.api.model.meta.instrument.PriceFormat;
import com.barchart.feed.meta.instrument.DefaultInstrument;
import com.barchart.util.value.ValueFactoryImpl;
import com.barchart.util.value.api.Fraction;
import com.barchart.util.value.api.Price;
import com.barchart.util.value.api.Size;
import com.barchart.util.value.api.ValueFactory;

public class ExtrasInstrument extends DefaultInstrument {

	protected static final ValueFactory factory = ValueFactoryImpl.getInstance();

	private final Map<VendorID, String> vendorSymbols = new HashMap<VendorID, String>();

	private InstrumentID id = InstrumentID.NULL;
	private SecurityType securityType = SecurityType.NULL_TYPE;
	private BookLiquidityType liquidityType = BookLiquidityType.NONE;
	private BookStructureType structureType = BookStructureType.NONE;
	private Size maxBookDepth = Size.NULL;
	private VendorID vendor = VendorID.NULL;
	private String symbol = "NULL";
	private String description = null;
	private String CFICode = null;
	private String exchangeCode = null;
	private Price tickSize = Price.NULL;
	private Price pointValue = Price.NULL;
	private final Fraction displayFraction = Fraction.NULL;
	private PriceFormat priceFormat = PriceFormat.NULL;
	private Calendar calendar = Calendar.NULL;

	public ExtrasInstrument() {
	}

	public ExtrasInstrument(final Element element) {
		// TODO parse extras response
	}

	@Override
	public InstrumentID id() {
		return id;
	}

	@Override
	public String marketGUID() {
		return symbol();
	}

	@Override
	public SecurityType securityType() {
		return securityType;
	}

	protected void securityType(final SecurityType type_) {
		securityType = type_;
	}

	@Override
	public BookLiquidityType liquidityType() {
		return liquidityType;
	}

	protected void liquidityType(final BookLiquidityType type_) {
		liquidityType = type_;
	}

	@Override
	public BookStructureType bookStructure() {
		return structureType;
	}

	protected void bookStructure(final BookStructureType type_) {
		structureType = type_;
	}

	@Override
	public Size maxBookDepth() {
		return maxBookDepth;
	}

	protected void maxBookDepth(final Size depth) {
		maxBookDepth = depth;
	}

	@Override
	public VendorID vendor() {
		return vendor;
	}

	protected void vendor(final VendorID vendor_) {
		vendor = vendor_;
	}

	@Override
	public Map<VendorID, String> vendorSymbols() {
		return vendorSymbols;
	}

	@Override
	public String symbol() {
		return symbol;
	}

	protected void symbol(final String symbol_) {
		symbol = symbol_;
		id = new InstrumentID(symbol_);
	}

	@Override
	public String description() {
		return description;
	}

	protected void description(final String description_) {
		description = description_;
	}

	@Override
	public String CFICode() {
		return CFICode;
	}

	protected void CFICode(final String code_) {
		CFICode = code_;
	}

	@Override
	public Exchange exchange() {
		return Exchanges.fromCode(exchangeCode);
	}

	@Override
	public String exchangeCode() {
		return exchangeCode;
	}

	protected void exchangeCode(final String code_) {
		exchangeCode = code_;
	}

	@Override
	public Price tickSize() {
		return tickSize;
	}

	protected void tickSize(final Price size_) {
		tickSize = size_;
	}

	@Override
	public Price pointValue() {
		return pointValue;
	}

	protected void pointValue(final Price value_) {
		pointValue = value_;
	}

	@Override
	public Fraction displayFraction() {
		return displayFraction;
	}

	@Override
	public PriceFormat priceFormat() {
		return priceFormat;
	}

	protected void priceFormat(final PriceFormat format_) {
		// TODO Set display fraction
		priceFormat = format_;
	}

	@Override
	public Calendar calendar() {
		return calendar;
	}

	protected void calendar(final Calendar calendar_) {
		calendar = calendar_;
	}

	// This logic needs to go in the XML parser
	// @Override
	// public long timeZoneOffset() {
	//
	// if(!def.hasTimeZoneName()) {
	// return 0l;
	// }
	//
	// /* Hack because extras "time zone name" isn't actually the code for the
	// * timezone */
	// final String tzn = timeZoneName();
	//
	// TimeZone zone;
	// if(tzn.equals("NEW_YORK")) {
	// zone = TimeZone.getTimeZone("EST");
	// } else if(tzn.equals("CHICAGO")) {
	// zone = TimeZone.getTimeZone("CST");
	// } else {
	// zone = TimeZone.getTimeZone(timeZoneName());
	// }
	//
	// return zone.getOffset(System.currentTimeMillis());
	// }

}
