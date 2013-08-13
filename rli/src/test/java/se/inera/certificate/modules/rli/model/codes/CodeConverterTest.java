package se.inera.certificate.modules.rli.model.codes;

import static org.junit.Assert.*;

import org.junit.Test;

import se.inera.certificate.model.Kod;

public class CodeConverterTest {

	@Test
	public void testCodeConverter() {
		
		Kod res = CodeConverter.toKod(TestKod.CODE_RED);
		
		assertNotNull(res);
		assertEquals("CODE_RED", res.getCode());
		assertEquals("CC", res.getCodeSystem());
		assertEquals("ColorCodes", res.getCodeSystemName());
		assertEquals("1.0", res.getCodeSystemVersion());
	}
	
	@Test(expected = IllegalArgumentException.class)	
	public void testCodeConverterIllegalInput() {
		
		CodeConverter.toKod(TestKod.CODE_BLACK);
		
	}
	
}
