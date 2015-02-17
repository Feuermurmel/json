package ch.feuermurmel.json;

import org.junit.Test;

import static ch.feuermurmel.json.Json.NULL;
import static ch.feuermurmel.json.Json.convert;
import static ch.feuermurmel.json.Json.map;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public final class JsonMapTest {
	private final JsonMap map1 = map().put("a", null).put("b", false).put("c", 0);
	
	@Test
	public void testPut() throws Exception {
		map1.put("d", "");
		
		assertThat(map1.size(), equalTo(4));
		assertThat(map1.has("d"), equalTo(true));
		assertThat(map1.get("d"), equalTo(convert("")));
	}
	
	@Test
	public void testGet() throws Exception {
		assertThat(map1.get("a"), equalTo(NULL));
		assertThat(map1.get("b"), equalTo(convert(false)));
		assertThat(map1.get("c"), equalTo(convert(0)));
		
		assertThat(map1.get("a", true), equalTo(NULL));
		assertThat(map1.get("b", true), equalTo(convert(false)));
		assertThat(map1.get("c", true), equalTo(convert(0)));
		
		assertThat(map1.get("d", true), equalTo(convert(true)));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGetFailure() throws Exception {
		assertThat(map1.get("d"), equalTo(NULL));
	}
	
	@Test
	public void testRemove() throws Exception {
		map1.remove("a");
		
		assertThat(map1.size(), equalTo(2));
		assertThat(map1.has("a"), equalTo(false));
		
		map1.remove("d");
		
		assertThat(map1.size(), equalTo(2));
	}
	
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
