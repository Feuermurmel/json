package ch.feuermurmel.json;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static ch.feuermurmel.json.Json.*;

@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@RunWith(Parameterized.class)
public final class WhiteSpaceTest {
	@Parameterized.Parameter
	public String whitespace;

	@Test
	public void testNumber() throws JsonParseException {
		parse(String.format("%s1%s", whitespace, whitespace));
	}

	@Test
	public void testList() throws JsonParseException {
		parse(String.format("[%s]", whitespace));
	}

	@Parameterized.Parameters
	public static List<Object[]> parameters() {
		return Arrays.asList(new Object[][] {
			{ "" },
			{ " " },
			{ "\t" },
			{ "\n" },
			{ "\r" },
			{ " \t\r\n" }
		});
	}
}
