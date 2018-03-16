package ch.feuermurmel.json;

import java.math.BigInteger;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static ch.feuermurmel.json.Json.convert;
import static ch.feuermurmel.json.Json.list;
import static ch.feuermurmel.json.Json.map;
import static ch.feuermurmel.json.Json.parse;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertThat;

@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@RunWith(Parameterized.class)
public final class ParseValuesTest {
	@Parameterized.Parameter(0)
	public Object value;
	
	@Parameterized.Parameter(1)
	public List<String> documents;
	
	@Test
	public void testParse() throws JsonParseException {
		for (String i : documents) {
			assertThat(parse(i), CoreMatchers.is(convert(value)));
		}
	}
	
	@Parameterized.Parameters
	public static List<Object[]> parameters() {
		// TODO: Add tests for string escapes.
		return asList(new Object[][] {
			{ null, asList("null", " null", " null ") },
			{ true, asList("true") },
			{ false, asList("false") },
			{ 0, asList("0", "-0") },
			{ 1, asList("1") },
			{ 1000000000000000000l, asList("1000000000000000000") },
			{ -1, asList("-1") },
			{ -1000000000000000000l, asList("-1000000000000000000") },
			{ (long) 1e10, asList("10000000000") },
			{ new BigInteger("14828023015344763744721114075"), asList("14828023015344763744721114075")},
			{ new BigInteger("-14828023015344763744721114075"), asList("-14828023015344763744721114075")},
			{ 0., asList("0.0", "0e0", "0e+0", "0e-0", "0e10", "-0.0", "-0e0", "-0e+0", "-0e-0", "-0e10") },
			{ 1., asList("1.0", "1.0e0", "1e0", "1e+0", "1e-0") },
			{ 1.5, asList("1.5", "1.5e0") },
			{ 1e10, asList("1e10", "1e+10") },
			{ 1e-10, asList("1e-10") },
			{ -1., asList("-1.0", "-1.0e0", "-1e0", "-1e+0", "-1e-0") },
			{ -1.5, asList("-1.5", "-1.5e0") },
			{ -1e10, asList("-1e10", "-1e+10") },
			{ -1e-10, asList("-1e-10") },
			{ "abc", asList("\"abc\"") },
			{ list(), asList("[]", " [ ] ") },
			{ list().add(1).add(2), asList("[1,2]", "[1, 2]", " [ 1 , 2 ] ") },
			{ map(), asList("{}") },
			{ map().put("a", 1).put("b", 2), asList("{\"a\":1, \"b\":2}") }
		});
	}
}
