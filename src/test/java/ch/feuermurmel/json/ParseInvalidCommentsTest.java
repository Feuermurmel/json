package ch.feuermurmel.json;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static ch.feuermurmel.json.Json.*;

@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@RunWith(Parameterized.class)
public final class ParseInvalidCommentsTest {
	@Parameterized.Parameter
	public String document;

	@Test(expected = JsonParseException.class)
	public void testParse() throws JsonParseException {
		parse(document);
	}

	@Parameterized.Parameters
	public static List<Object[]> parameters() {
		// TODO: Add tests for more invalid strings (especially invalid escapes).
		return Arrays
			.asList(
				"/*/ null",
				"// null",
				"[//]",
				"/** */*/",
				"//")
			.stream()
			.map(x -> new Object[] { x })
			.collect(Collectors.toList());
	}
}
