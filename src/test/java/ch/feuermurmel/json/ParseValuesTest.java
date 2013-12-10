package ch.feuermurmel.json;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static ch.feuermurmel.json.Json.*;
import static org.junit.Assert.assertThat;

@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@RunWith(Parameterized.class)
public final class ParseValuesTest {
	@Parameterized.Parameter(0)
	public Object value;
	
	@Parameterized.Parameter(1)
	public String[] documents;

	@Test
	public void testParse() throws JsonParseException {
		for (String i : documents) {
			assertThat(parse(i), CoreMatchers.is(convert(value)));
		}
	}

	@Parameterized.Parameters
	public static List<Object[]> parameters() {
		// TODO: Add more test for different number formats and string escapes.
		return Arrays.asList(new Object[][] {
			{ null, new String[]{ "null", " null", " null " } },
			{ true, new String[]{ "true" } },
			{ false, new String[]{ "false" } },
			{ 0, new String[]{ "0", "-0" } },
			{ 1, new String[]{ "1" } },
			{ (long) 1e10, new String[]{ "10000000000" } },
			{ 0., new String[]{ "0.0", "-0.0", "0e0", "0e+0", "0e-0", "0e10" } },
			{ 1., new String[]{ "1.0", "1.0e0", "1e0", "1e+0", "1e-0" } },
			{ 1.5, new String[]{ "1.5", "1.5e0" } },
			{ 1e10, new String[]{ "1e10", "1e+10" } },
			{ 1e-10, new String[]{ "1e-10" } },
			{ "abc", new String[]{ "\"abc\"" } },
			{ list(), new String[]{ "[]", " [ ] " } },
			{ list().add(1).add(2), new String[]{ "[1,2]", "[1, 2]", " [ 1 , 2 ] " } },
			{ map(), new String[]{ "{}" } },
			{ map().put("a", 1).put("b", 2), new String[]{ "{\"a\":1, \"b\":2}" } }
		});
	}
}
