package ch.feuermurmel.json;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static ch.feuermurmel.json.Json.convert;

@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@RunWith(Parameterized.class)
public final class InvalidConversionsTest {
	@Parameterized.Parameter(0)
	public Object javaValue;
	
	@Parameterized.Parameter(1)
	public ValidConversionsTest.Coercion invalidCoercion;
	
	@Test(expected = UnsupportedTypeException.class)
	public void testCoercion() {
		invalidCoercion.coerce(convert(javaValue));
	}
	
	@Parameterized.Parameters
	public static List<Object[]> parameters() {
		List<Object[]> res = new ArrayList<>();
		
		for (ValidConversionsTest.ValidCoercions i : ValidConversionsTest.validCoercions) {
			Set<ValidConversionsTest.Coercion> invalidCoercions = EnumSet.allOf(ValidConversionsTest.Coercion.class);
			
			invalidCoercions.removeAll(i.validCoercions);
			
			for (ValidConversionsTest.Coercion j : invalidCoercions) {
				res.add(new Object[] { i.javaValue, j });
			}
		}
		
		return res;
	}
}
