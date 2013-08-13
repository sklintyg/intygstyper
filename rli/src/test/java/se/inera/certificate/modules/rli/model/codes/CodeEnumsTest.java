package se.inera.certificate.modules.rli.model.codes;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.theories.suppliers.TestedOn;

public class CodeEnumsTest {

	@Test
	public void testArrangemangsTyp() {

		String code = "420008001";
		
		ArrangemangsTyp res = ArrangemangsTyp.getFromCode(code);
		
		assertNotNull(res);
		assertEquals(code, res.getCode());
	}
	
	@Test
	public void testObservationsKod() {
		
		String code = "39104002";
		
		ObservationsKod res = ObservationsKod.getFromCode(code);
		
		assertNotNull(res);
		assertEquals(code, res.getCode());
	}
	
	@Test
	public void testSjukdomsKannedom() {
		
		String code = "SJK1";
		
		SjukdomsKannedom res = SjukdomsKannedom.getFromCode(code);
		
		assertNotNull(res);
		assertEquals(code, res.getCode());
	}
	
	@Test
	public void testRekommendationsKod() {
		
		String code = "REK2";
		
		RekommendationsKod res = RekommendationsKod.getFromCode(code);
		
		assertNotNull(res);
		assertEquals(code, res.getCode());
	}
	
}
