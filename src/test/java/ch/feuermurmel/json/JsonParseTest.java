package ch.feuermurmel.json;

import java.util.Arrays;

import org.junit.Test;

@SuppressWarnings("PublicConstructor")
public final class JsonParseTest {
	@Test
	public void testWhiteSpace() throws JsonParseException {
		for (String i : Arrays.asList("", " ", "\t", "\n", "\r", " \t\r\n")) {
			Json.parse(String.format("%s1%s", i, i));
			Json.parse(String.format("[%s]", i));
		}
	}

	//private static void checkEquals(JsonObject obj, JsonObject expected) {
	//	if (expected instanceof JsonNull) {
	//		Assert.assertThat(obj, CoreMatchers.instanceOf(JsonNull.class));
	//	} else if (expected instanceof JsonNumber) {
	//		Assert.assertThat(obj, CoreMatchers.instanceOf(JsonNumber.class));
	//
	//		if (((JsonNumber) expected).isIntegral()) {
	//			Assert.assertThat(obj.isin);
	//		}
	//	}
	//}
}
