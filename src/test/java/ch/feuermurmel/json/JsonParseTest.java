package ch.feuermurmel.json;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static ch.feuermurmel.json.Json.*;
import static ch.feuermurmel.json.TestUtil.expectException;
import static org.junit.Assert.assertThat;

@SuppressWarnings("PublicConstructor")
public final class JsonParseTest {
	@Test
	public void testWhiteSpace() throws JsonParseException {
		for (String i : Arrays.asList("", " ", "\t", "\n", "\r", " \t\r\n")) {
			parse(String.format("%s1%s", i, i));
			parse(String.format("[%s]", i));
		}
	}

	@Test
	public void testParseValues() throws Exception {
		// TODO: Add more test for different number formats and string escapes.
		
		checkParse(null, "null", " null", " null ");
		checkParse(true, "true");
		checkParse(false, "false");
		checkParse(0, "0", "-0");
		checkParse(1, "1");
		checkParse((long) 1e10, "10000000000");
		checkParse(0., "0.0", "-0.0", "0e0", "0e+0", "0e-0", "0e10");
		checkParse(1., "1.0", "1.0e0", "1e0", "1e+0", "1e-0");
		checkParse(1.5, "1.5", "1.5e0");
		checkParse(1e10, "1e10", "1e+10");
		checkParse(1e-10, "1e-10");
		checkParse("abc", "\"abc\"");
		checkParse(list(), "[]", " [ ] ");
		checkParse(list().add(1).add(2), "[1,2]", "[1, 2]", " [ 1 , 2 ] ");
		checkParse(map(), "{}");
		checkParse(map().put("a", 1).put("b", 2), "{\"a\":1, \"b\":2}");
	}

	@Test
	public void testParseFailures() throws Exception {
		// TODO: Add tests for more invalid strings (especially invalid escapes).
		List<String> documents = Arrays.asList(
			"",
			"[",
			"[,]",
			"[1,]",
			"[,1]",
			"[1,,2]",
			"{,}",
			"{\"a\":}",
			"{\"a\":1,}",
			"{1:2}",
			"{a:1}",
			"{\"a\":1,2}",
			"{\"a\":1,,\"b\":2}",
			"Null",
			"True",
			"False",
			"\"",
			"\"abc",
			"1.",
			".1",
			"1. 1",
			"1 .1",
			"1e",
			"1.e1",
			".1e1",
			"1e1.",
			"1e.1",
			"+1",
			"00"
		);
		
		for (final String i : documents) {
			expectException(JsonParseException.class, new TestUtil.TestRunnable() {
				@Override
				public void run() throws Throwable {
					parse(i);
				}
			});
		}
	}

	private static void checkParse(Object expected, String... documents) throws JsonParseException {
		for (String i : documents) {
			assertThat(parse(i), CoreMatchers.is(convert(expected)));
		}
	}
}
