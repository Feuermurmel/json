package ch.feuermurmel.json;

import org.junit.Assert;
import org.junit.Test;

import static ch.feuermurmel.json.Json.convert;

public final class ConvertTest {
	@Test
	public void convertInt() throws Exception {
		Assert.assertEquals(new JsonLong(1), convert(1));
	}
	
	@Test
	public void convertBoxedInt() throws Exception {
		Assert.assertEquals(new JsonLong(1), convert((Object) 1));
	}
	
	@Test
	public void convertString() throws Exception {
		Assert.assertEquals(new JsonString("a"), convert((Object) "a"));
	}
	
	@Test
	public void convertStringDirect() throws Exception {
		Assert.assertEquals(new JsonString("a"), convert("a"));
	}
	
	@Test
	public void convertNull() throws Exception {
		Assert.assertEquals(JsonNull.instance, convert((Object) null));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void convertStringNull() throws Exception {
		convert((String) null);
	}
	
	@Test
	public void convertDouble() throws Exception {
		Assert.assertEquals(new JsonDouble(1.1), convert(1.1));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void convertInvalidDouble1() throws Exception {
		convert(Double.NEGATIVE_INFINITY);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void convertInvalidFloat() throws Exception {
		convert(Float.NEGATIVE_INFINITY);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void convertInvalidDouble2() throws Exception {
		convert(Math.sqrt(-1));
	}
}
