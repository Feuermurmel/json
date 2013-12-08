package ch.feuermurmel.json;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public final class JsonMapTest {
	private final JsonMap map1 = Json.map().put("a", null).put("b", false).put("c", 0);

	@Test
	public void testPut() throws Exception {
		map1.put("d", "");

		assertThat(map1.size(), equalTo(4));
		assertThat(map1.has("d"), equalTo(true));
		assertThat(map1.get("d"), equalTo(Json.convert("")));
	}

	@Test
	public void testGet() throws Exception {
		assertThat(map1.get("a"), equalTo(Json.convert(null)));
		assertThat(map1.get("b"), equalTo(Json.convert(false)));
		assertThat(map1.get("c"), equalTo(Json.convert(0)));

		assertThat(map1.get("a", true), equalTo(Json.convert(null)));
		assertThat(map1.get("b", true), equalTo(Json.convert(false)));
		assertThat(map1.get("c", true), equalTo(Json.convert(0)));

		assertThat(map1.get("d", true), equalTo(Json.convert(true)));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetFailure() throws Exception {
		assertThat(map1.get("d"), equalTo(Json.convert(null)));
	}

	@Test
	public void testRemove() throws Exception {
		map1.remove("a");

		assertThat(map1.size(), equalTo(2));
		assertThat(map1.has("a"), equalTo(false));

		map1.remove("d");

		assertThat(map1.size(), equalTo(2));
	}

	//@Test(expected = IllegalArgumentException.class)
	//public void testRemoveFailure() throws Exception {
	//	map1.remove("d");
	//}

	@Test
	public void testHas() throws Exception {
		assertThat(map1.has("a"), equalTo(true));
		assertThat(map1.has("d"), equalTo(false));
	}

	@Test
	public void testSize() throws Exception {
		assertThat(map1.size(), equalTo(3));
	}
}
