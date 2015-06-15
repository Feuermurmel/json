package ch.feuermurmel.json;

import java.util.Arrays;

import org.junit.Test;

import static ch.feuermurmel.json.Json.list;
import static ch.feuermurmel.json.Json.map;
import static org.junit.Assert.assertEquals;

public final class PrettyPrintTest {
	@Test
	public void test1() throws Exception {
		checkPrettyPrint(1, list());
	}
	
	@Test
	public void test2() throws Exception {
		checkPrettyPrint(1, list(1, 2, 3));
	}
	
	@Test
	public void test3() throws Exception {
		checkPrettyPrint(6, list(1, 2, 3, 4));
	}
	
	@Test
	public void test4() throws Exception {
		checkPrettyPrint(1, list(list(), list(), list()));
	}
	
	@Test
	public void test5() throws Exception {
		checkPrettyPrint(6, list(list(), list(), list(), map()));
	}
	
	@Test
	public void test6() throws Exception {
		checkPrettyPrint(6, list(list(1, 2, 3), list(1, 2, 3), list(1, 2, 3), map()));
	}
	
	@Test
	public void test7() throws Exception {
		checkPrettyPrint(1, map());
	}
	
	@Test
	public void test8() throws Exception {
		checkPrettyPrint(1, map().put("a", 1));
	}
	
	@Test
	public void test9() throws Exception {
		checkPrettyPrint(4, map().put("a", 1).put("b", 2));
	}
	
	@Test
	public void test10() throws Exception {
		checkPrettyPrint(1, map().put("a", list(1)));
	}
	
	@Test
	public void test11() throws Exception {
		checkPrettyPrint(3, map().put("a", list(1, 2)));
	}
	
	@Test
	public void test12() throws Exception {
		checkPrettyPrint(8, map().put("a", list(1, 2, 3, 4)));
	}
	
	static void checkPrettyPrint(long expectedLineCount, JsonObject value) {
		String prettyString = value.prettyPrint().setMaxTokensPerLine(3).toString();
		
		try {
			assertEquals(value, Json.parse(prettyString));
		} catch (JsonParseException e) {
			throw new AssertionError(e);
		}
		
		// Ignore the empty "line" at the end.
		long lineCount = Arrays.asList(prettyString.split("\n")).stream().filter(x -> !x.isEmpty()).count();
		
		assertEquals(expectedLineCount, lineCount);
	}
}
