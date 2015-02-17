package ch.feuermurmel.json;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static ch.feuermurmel.json.Json.convert;
import static ch.feuermurmel.json.Json.list;
import static ch.feuermurmel.json.Json.parse;
import static org.junit.Assert.assertThat;

@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@RunWith(Parameterized.class)
public final class ParseCommentsTest {
	@Parameterized.Parameter(0)
	public Object value;
	
	@Parameterized.Parameter(1)
	public String document;
	
	@Test
	public void testParse() throws JsonParseException {
		assertThat(parse(document), CoreMatchers.is(convert(value)));
	}
	
	private static final List<Object[]> cases = new ArrayList<>();
	
	static {
		addCase(null, "null/**/", "/***/ null /****/", "null //", "/*\n*/ null");
		addCase(list(), "/**/[/**/]/**/", "//*/\n[]");
	}
	
	@SuppressWarnings("ReturnOfCollectionOrArrayField")
	@Parameterized.Parameters
	public static List<Object[]> getParameters() {
		return cases;
	}
	
	private static void addCase(Object value, String... documents) {
		for (String i : documents) {
			cases.add(new Object[] { value, i });
		}
	}
}
