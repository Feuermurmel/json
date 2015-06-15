package ch.feuermurmel.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Helper class for creating {@link JsonObject} instances.
 */
public final class Json {
	private Json() {
	}
	
	@SuppressWarnings("ConstantNamingConvention")
	public static final JsonObject NULL = JsonNull.instance;
	
	private static final Charset defaultCharset = Charset.forName("utf-8");
	
	/**
	 * Returns a new, empty {@link JsonList}. Useful using a static import for this class. This method is useful when used in a way like this:
	 * <p/>
	 * {@code JsonList list = list().add(1).add(2).add("three");}
	 *
	 * @return a new, empty JsonList.
	 */
	public static JsonList list() {
		return new JsonListImpl();
	}
	
	public static JsonList list(Object... elements) {
		return convert(Arrays.asList(elements));
	}
	
	/**
	 * Returns a new, empty {@link JsonMap}. Useful using a static import for this class. This method is useful when used in a way like this:
	 * <p/>
	 * {@code JsonMap map = map().add("one", 1).add("two", 2);}
	 *
	 * @return a new, empty JsonList.
	 */
	public static JsonMap map() {
		return new JsonMapImpl();
	}
	
	/**
	 * Convert a {@code boolean} value to a JSON boolean.
	 *
	 * @param value The value to convert.
	 */
	public static JsonObject convert(boolean value) {
		return value ? JsonBoolean.trueInstance : JsonBoolean.falseInstance;
	}
	
	/**
	 * Convert a {@code long} value to an integral JSON number.
	 *
	 * @param value The value to convert.
	 */
	public static JsonObject convert(long value) {
		return new JsonLong(value);
	}
	
	/**
	 * Convert a {@code double} to a floating JSON number.
	 *
	 * @param value The value to convert.
	 */
	public static JsonObject convert(double value) {
		return new JsonDouble(value);
	}
	
	/**
	 * Convert a {@code char} to a single-character JSON string.
	 *
	 * @param value The value to convert.
	 */
	public static JsonObject convert(char value) {
		return new JsonString(String.valueOf(value));
	}
	
	/**
	 * Convert a {@link String} to a JSON string.
	 *
	 * @param value The value to convert.
	 */
	public static JsonObject convert(String value) {
		return new JsonString(value);
	}
	
	/**
	 * Convert a {@link List} to a JSON list.
	 * <p/>
	 * The entries will be converted using {@link #convert(Object)}.
	 *
	 * @param value The value to convert.
	 */
	public static JsonList convert(Iterable<?> value) {
		JsonListImpl list = new JsonListImpl();
		
		for (Object i : value) {
			list.add(i);
		}
		
		return list;
	}
	
	/**
	 * Convert a {@link Map} to a JSON map.
	 * <p/>
	 * The values of the entries will be converted using {@link #convert(Object)}.
	 *
	 * @param value The value to convert.
	 */
	public static JsonMap convert(Map<String, ?> value) {
		JsonMap map = new JsonMapImpl();
		
		for (Map.Entry<String, ?> i : value.entrySet()) {
			map.put(i.getKey(), i.getValue());
		}
		
		return map;
	}
	
	/**
	 * This method either casts or converts an object to an implementation {@link JsonObject}.
	 * <p/>
	 * If the passed object is a subclass of JsonObject it will be cast. If the object implements {@link JsonConvertible}, it's {@link JsonConvertible#toJson()} method wil be called. Other types will be converted according to these rules:
	 * <p/>
	 * - {@code null} will be converted to {@link Json#NULL}.
	 * <p/>
	 * - Instances of {@link Boolean} will be converted using {@link #convert(boolean)}.
	 * <p/>
	 * - Instances of {@link Byte}, {@link Short}, {@link Integer} and {@link Long} will be converted using {@link #convert(long)}.
	 * <p/>
	 * - Instances of {@link Float} and {@link Double} will be converted using {@link #convert(double)}.
	 * <p/>
	 * - Instances of {@link Character} and {@link String} will be converted using {@link #convert(char)} and {@link #convert(String)}.
	 * <p/>
	 * - Instances of {@link Iterable} (e.g. {@link List}) will be converted using {@link #convert(Iterable)}.
	 * <p/>
	 * - Instances of {@link Map} will be converted similarly to {@link #convert(Map)}. In addition, keys are checked to be instances of {@link String}.
	 *
	 * @param value The object to convert.
	 *
	 * @throws UnsupportedTypeException when the argument or any of it's members cannot be converted to a JsonObject.
	 */
	public static JsonObject convert(Object value) {
		// convertibles
		if (value instanceof JsonConvertible) {
			return ((JsonConvertible) value).toJson();
		}
		
		// special null treatment
		if (value == null) {
			return JsonNull.instance;
		}
		
		// booleans
		if (value instanceof Boolean) {
			return convert((boolean) value);
		}
		
		// numbers
		if (value instanceof Byte) {
			return convert((byte) value);
		}
		
		if (value instanceof Short) {
			return convert((short) value);
		}
		
		if (value instanceof Integer) {
			return convert((int) value);
		}
		
		if (value instanceof Long) {
			return convert((long) value);
		}
		
		if (value instanceof Float) {
			return convert((float) value);
		}
		
		if (value instanceof Double) {
			return convert((double) value);
		}
		
		// strings
		if (value instanceof Character) {
			return convert((char) value);
		}
		
		if (value instanceof String) {
			return convert((String) value);
		}
		
		// maps
		if (value instanceof Map) {
			return convertMap((Map<?, ?>) value);
		}
		
		// lists
		if (value instanceof Iterable) {
			return convert((Iterable<?>) value);
		}
		
		throw new UnsupportedTypeException(String.format("Objects of type %s can't be converted to a JsonObject.", value.getClass().getName()));
	}
	
	/**
	 * Parse a JSON document into a {@link AbstractJsonObject}.
	 * <p/>
	 * Will parse the document according to the JSON document syntax. Can also be used on the output of {@link AbstractJsonObject#toString()} or {@link AbstractJsonObject#prettyPrint()}.
	 *
	 * @param input The string to parse. May contain leading or trailing whitespace.
	 *
	 * @return the parsed {@link AbstractJsonObject}.
	 *
	 * @throws JsonParseException if invalid syntax is encountered.
	 */
	public static JsonObject parse(String input) throws JsonParseException {
		return parse(input, null);
	}
	
	/**
	 * Like {@link #parse(String)} but with the additional ability to specify a file name or description of the source for error messages.
	 *
	 * @param sourceInfo Source description used in error messages.
	 */
	public static JsonObject parse(String input, String sourceInfo) throws JsonParseException {
		try {
			return parse(new StringReader(input), sourceInfo);
		} catch (IOException e) {
			throw new RuntimeException(e); // should never happen as we're reading from a string
		}
	}
	
	/**
	 * Parse a JSON document read from a {@link Reader}.
	 * <p/>
	 * Currently, the stream has to end after the document or parsing will fail.
	 *
	 * @param input Source to read from, e.g. an {@link InputStreamReader}.
	 *
	 * @return the parsed {@link AbstractJsonObject}.
	 *
	 * @throws JsonParseException if invalid syntax is encountered.
	 * @throws IOException it the passed {@code Reader} throws an {@code IOException}.
	 */
	public static JsonObject parse(Reader input) throws IOException, JsonParseException {
		return parse(input, null);
	}
	
	/**
	 * Like {@link #parse(Reader)} but with the additional ability to specify a file name or description of the source for error messages.
	 *
	 * @param sourceInfo Source description used in error messages.
	 */
	public static JsonObject parse(Reader input, String sourceInfo) throws IOException, JsonParseException {
		return Parser.runParser(input, sourceInfo);
	}
	
	/**
	 * Load a JSON document from a file.
	 * <p/>
	 * The file will be decoded using the UTF-8 encoding.
	 *
	 * @param file File that should be read.
	 *
	 * @return Returns the parsed JSON document.
	 *
	 * @throws IOException When loading fails because of a network or other IO error.
	 * @throws JsonParseException When parsing of the received/loaded JSON document failed. This may for example happen if the server closes the connection after sending a successful response code.
	 */
	public static JsonObject load(File file) throws IOException, JsonParseException {
		return load(file.toURI().toURL());
	}
	
	/**
	 * Load a JSON document from the resource identified by the specified URL. This may be used, for example, to load a document from a web server using an HTTP URL or from a resource using a URL obtained using {@link Class#getResource(String)}.
	 * <p/>
	 * The file will be decoded using the UTF-8 encoding.
	 *
	 * @param url URL identifying the resource from which to load the JSON document.
	 *
	 * @return Returns the parsed JSON document.
	 *
	 * @throws IOException When loading fails because of a network or other IO error.
	 * @throws JsonParseException When parsing of the received/loaded JSON document failed. This may for example happen if the server closes the connection after sending a successful response code.
	 */
	public static JsonObject load(URL url) throws IOException, JsonParseException {
		try (InputStream inputStream = url.openStream()) {
			return parse(new InputStreamReader(inputStream, defaultCharset), url.toString());
		}
	}
	
	/**
	 * Helper method that checks the keys of the map.
	 */
	private static JsonMap convertMap(Map<?, ?> obj) {
		JsonMap map = new JsonMapImpl();
		
		for (Map.Entry<?, ?> i : ((Map<?, ?>) obj).entrySet()) {
			Object key = i.getKey();
			
			if (!(key instanceof String)) {
				throw new UnsupportedTypeException(String.format("Objects of type %s can't be used as key in a JSON map.", i.getClass()));
			}
			
			map.put((String) key, i.getValue());
		}
		
		return map;
	}
}
