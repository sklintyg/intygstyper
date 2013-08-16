package se.inera.certificate.modules.rli.model.converters;

import static org.junit.Assert.*;

import org.joda.time.DateTimeFieldType;
import org.joda.time.Partial;
import org.junit.Test;

public class PartialConverterTest {
	
	@Test
	public void testPartialToStringWithYear() {
		
		Partial partial = new Partial().with(DateTimeFieldType.year(), 2013);
				
		String partialAsString = PartialConverter.partialToString(partial);
		
		assertNotNull(partialAsString);
		assertEquals("2013", partialAsString);
	}
	
	@Test
	public void testPartialToStringWithYearAndMonth1() {
		
		
		Partial partial = new Partial()
			.with(DateTimeFieldType.year(), 2013)
			.with(DateTimeFieldType.monthOfYear(), 3);
				
		String partialAsString = PartialConverter.partialToString(partial);
		
		assertNotNull(partialAsString);
		assertEquals("2013-03", partialAsString);
	}
	
	@Test
	public void testPartialToStringWithYearAndMonth2() {
		
		Partial partial = new Partial()
			.with(DateTimeFieldType.year(), 2013)
			.with(DateTimeFieldType.monthOfYear(), 12);
				
		String partialAsString = PartialConverter.partialToString(partial);
		
		assertNotNull(partialAsString);
		assertEquals("2013-12", partialAsString);
	}
	
	@Test
	public void testPartialToStringWithYearAndMonthAndDay1() {
		
		Partial partial = new Partial()
			.with(DateTimeFieldType.year(), 2013)
			.with(DateTimeFieldType.monthOfYear(), 4)
			.with(DateTimeFieldType.dayOfMonth(), 5);
				
		String partialAsString = PartialConverter.partialToString(partial);
		
		assertNotNull(partialAsString);
		assertEquals("2013-04-05", partialAsString);
	}
	
	@Test
	public void testPartialToStringWithYearAndMonthAndDay2() {
		
		Partial partial = new Partial()
			.with(DateTimeFieldType.year(), 2008)
			.with(DateTimeFieldType.monthOfYear(), 12)
			.with(DateTimeFieldType.dayOfMonth(), 11);
				
		String partialAsString = PartialConverter.partialToString(partial);
		
		assertNotNull(partialAsString);
		assertEquals("2008-12-11", partialAsString);
	}
	
	@Test
	public void testStringToPartialWithNullAndEmptyString() {
		
		Partial partial;
		
		partial = PartialConverter.stringToPartial(null);
		assertNull(partial);
		
		partial = PartialConverter.stringToPartial("");
		assertNull(partial);
		
		partial = PartialConverter.stringToPartial("  ");
		assertNull(partial);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testStringToPartialWithGarbage() {
		
		PartialConverter.stringToPartial("goobledygook");
		
	}
	
	@Test
	public void testStringToPartialWithYear() {
		
		String dateStr = "2013 ";
		
		Partial partial = PartialConverter.stringToPartial(dateStr);
		
		assertNotNull(partial);
		assertTrue("Year should be supported", partial.isSupported(DateTimeFieldType.year()));
	}
	
	@Test
	public void testStringToPartialWithYearAndMonth() {
		
		String dateStr = " 2015-05 ";
		
		Partial partial = PartialConverter.stringToPartial(dateStr);
		
		assertNotNull(partial);
		assertTrue("Year should be supported", partial.isSupported(DateTimeFieldType.year()));
		assertTrue("Month should be supported", partial.isSupported(DateTimeFieldType.monthOfYear()));
	}
	
	@Test
	public void testStringToPartialWithYearAndMonthAndDay() {
		
		String dateStr = "  1997-12-12 ";
		
		Partial partial = PartialConverter.stringToPartial(dateStr);
		
		assertNotNull(partial);
		assertTrue("Year should be supported", partial.isSupported(DateTimeFieldType.year()));
		assertTrue("Month should be supported", partial.isSupported(DateTimeFieldType.monthOfYear()));
		assertTrue("Month should be supported", partial.isSupported(DateTimeFieldType.dayOfMonth()));
	}
	
	@Test
	public void testRoundTripWithYear() {
		
		String inputDateStr = "2011";
		
		Partial partial = PartialConverter.stringToPartial(inputDateStr);
		
		assertNotNull(partial);
		
		String outputDateStr = PartialConverter.partialToString(partial);
		
		assertNotNull(outputDateStr);
		assertEquals("In and out does not match", inputDateStr, outputDateStr);
	}
	
	@Test
	public void testRoundTripWithYearMonth() {
		
		String inputDateStr = "2003-10";
		
		Partial partial = PartialConverter.stringToPartial(inputDateStr);
		
		assertNotNull(partial);
		
		String outputDateStr = PartialConverter.partialToString(partial);
		
		assertNotNull(outputDateStr);
		assertEquals("In and out does not match", inputDateStr, outputDateStr);
	}
	
	@Test
	public void testRoundTripWithYearMonthDay() {
		
		String inputDateStr = "2012-07-14";
		
		Partial partial = PartialConverter.stringToPartial(inputDateStr);
		
		assertNotNull(partial);
		
		String outputDateStr = PartialConverter.partialToString(partial);
		
		assertNotNull(outputDateStr);
		assertEquals("In and out does not match", inputDateStr, outputDateStr);
	}
	
}
