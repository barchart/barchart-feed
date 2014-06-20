package com.barchart.feed.base.market.provider;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

import com.barchart.feed.base.provider.Symbology;
import com.barchart.feed.base.provider.Symbology.ExpireMonth;

public class SymbologyTest {

	@Test
	public void testFormatOptionSymbols() {
		try {
			setFinalStaticFieldValue(Symbology.class.getDeclaredField("YEAR"), 2014);
			setFinalStaticFieldValue(Symbology.class.getDeclaredField("MONTH"), ExpireMonth.JUN.code);
			
			///////////
			
			//Test month < June - implying next year
			String test1 = "GCK1975C";
			String out = Symbology.formatSymbol(test1);
			assertEquals("GCK2015|1975C", out);
			
			//Test S.A.A. for puts
			test1 = "GCK1975P";
			out = Symbology.formatSymbol(test1);
			assertEquals("GCK2015|1975P", out);
			
			//////////
			
			//Test same in same out
			test1 = "GCM1975C";
			out = Symbology.formatSymbol(test1);
			assertEquals("GCM2014|1975C", out);
			
			//Test S.A.A. for puts
			test1 = "GCM1975P";
			out = Symbology.formatSymbol(test1);
			assertEquals("GCM2014|1975P", out);
			
			
			/////////// Run through all years //////////
			
			String input = "GCM1975";
			String inputCall = "C";
			String inputPut = "P";
			
			String fullPrefix = "GCM";
			String fullSuffix = "|1975";
			for(int i = 67, y = 2014;i < 91;i++, y = i == 80 ? 2014 : y + 1) {
				y = i == 80 ? 2014 : y; 
				String sym = input + ((char)i);
				String symOut = Symbology.formatSymbol(sym);
				assertEquals(fullPrefix + y + fullSuffix + (i < 80 ? inputCall : inputPut), symOut);
				
			}
		}catch(Exception e) {
			fail();
		}
	}
	
	/**
	 * Change the value of the static field for test predictability
	 * @param field
	 * @param value
	 * @throws Exception
	 */
	public static void setFinalStaticFieldValue(Field field, Object value) throws Exception {
		field.setAccessible(true);
		
		Field modifiersField = Field.class.getDeclaredField("modifiers");
	    modifiersField.setAccessible(true);
	    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

	    field.set(null, value);
	}

}
