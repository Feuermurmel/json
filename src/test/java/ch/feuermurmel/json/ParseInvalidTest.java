package ch.feuermurmel.json;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static ch.feuermurmel.json.Json.parse;

@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@RunWith(Parameterized.class)
public final class ParseInvalidTest {
	@Parameterized.Parameter
	public String document;
	
	@Test(expected = JsonParseException.class)
	public void testParse() throws JsonParseException {
		parse(document);
	}
	
	@Parameterized.Parameters
	public static List<Object[]> parameters() {
		// TODO: Add tests for more invalid strings (especially invalid escapes).
		return Arrays.asList(new Object[][] {
			{ "", },
			{ "[", },
			{ "[,]", },
			{ "[1,]", },
			{ "[,1]", },
			{ "[1,,2]", },
			{ "{,}", },
			{ "{\"a\":}", },
			{ "{\"a\":1,}", },
			{ "{1:2}", },
			{ "{a:1}", },
			{ "{\"a\":1,2}", },
			{ "{\"a\":1,,\"b\":2}", },
			{ "Null", },
			{ "True", },
			{ "False", },
			{ "\"", },
			{ "\"abc", },
			{ "1.", },
			{ ".1", },
			{ "1. 1", },
			{ "1 .1", },
			{ "1e", },
			{ "1.e1", },
			{ ".1e1", },
			{ "1e1.", },
			{ "1e.1", },
			{ "+1", },
			{ "00" }
		});
	}
}
