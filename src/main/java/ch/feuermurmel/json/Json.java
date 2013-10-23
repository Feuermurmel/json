package ch.feuermurmel.json;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Helper class for working with ch.feuermurmel.json.* instances and creating new instances.
 */
public final class Json {
	private Json() {
	}

	/**
	 * Shortcut for {@link JsonList#create()}.
	 * <p/>
	 * Returns a new, empty {@link JsonList}. Useful using a static import for this class. This method is useful when used in a way like this:
	 * <p/>
	 * {@code JsonList list = list().add(1).add(2).add("three");}
	 *
	 * @return a new, empty JsonList.
	 */
	public static JsonList list() {
		return JsonList.create();
	}

	/**
	 * Shortcut for {@link JsonMap#create()}.
	 * <p/>
	 * Returns a new, empty {@link JsonMap}. Useful using a static import for this class. This method is useful when used in a way like this:
	 * <p/>
	 * {@code JsonMap map = map().add("one", 1).add("two", 2);}
	 *
	 * @return a new, empty JsonList.
	 */
	public static JsonMap map() {
		return JsonMap.create();
	}

	/**
	 * This method either casts or converts an object to a subclass of {@link JsonObject}.
	 * <p/>
	 * If the passed object is a subclass of JsonObject it will be cast. If the object implements {@link JsonConvertible}, it's {@link JsonConvertible#toJson()} method wil be called. Other types will be converted according to this list:
	 * <p/>
	 * - {@code null} will be converted to {@link JsonNull}.
	 * <p/>
	 * - {@code boolean} and instances of {@code Boolean} will be converted to {@link JsonBoolean}.
	 * <p/>
	 * - {@code byte}, {@code short}, {@code int}, {@code long}, {@code float}, {@code double} and instances of their boxed variants will be converted to {@link JsonNumber}.
	 * <p/>
	 * - {@code char} and instances of {@code Character} and {@code String} will be converted to {@link JsonString}.
	 * <p/>
	 * - Instances of {@link Map} will be converted to {@link JsonMap}. Their keys will be converted using {@code toString()}, their values using this method.
	 * <p/>
	 * - Instances of {@link Iterable} will be converted to {@link List}. Their elements will be converted using this method.
	 *
	 * @param obj The object to convert.
	 * @throws UnsupportedTypeException when the argument or any of it's members cannot be converted to a JsonObject.
	 */
	public static JsonObject convert(Object obj) {
		// convertibles
		if (obj instanceof JsonConvertible)
			return ((JsonConvertible) obj).toJson();

		// special null treatment
		if (obj == null)
			return JsonNull.instance;

		// booleans
		if (obj instanceof Boolean)
			return JsonBoolean.instance((Boolean) obj);

		// numbers
		if (obj instanceof Byte)
			return JsonNumber.instance((Byte) obj);

		if (obj instanceof Short)
			return JsonNumber.instance((Short) obj);

		if (obj instanceof Integer)
			return JsonNumber.instance((Integer) obj);

		if (obj instanceof Long)
			return JsonNumber.instance((Long) obj);

		if (obj instanceof Float)
			return JsonNumber.instance((Float) obj);

		if (obj instanceof Double)
			return JsonNumber.instance((Double) obj);

		// strings
		if (obj instanceof Character)
			return JsonString.instance(obj.toString());

		if (obj instanceof String)
			return JsonString.instance((String) obj);

		// maps
		if (obj instanceof Map)
			return JsonMap.create((Map<?, ?>) obj);

		// lists
		if (obj instanceof Iterable)
			return JsonList.create((Iterable<?>) obj);

		throw new UnsupportedTypeException("Objects of type " + obj.getClass().getName() + " can't be converted to a JsonObject!");
	}

	/**
	 * Parse a JSON document into a {@link JsonObject}.
	 * <p/>
	 * Will parse the document according to the JSON document syntax. Can also be used on the output of {@link JsonObject#toString()} or {@link JsonObject#prettyPrint()}.
	 *
	 * @param input The string to parse. May contain leading or trailing whitespace.
	 * @return the parsed {@link JsonObject}.
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
	 * @return the parsed {@link JsonObject}.
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
	 * @return Returns the parsed JSON document.
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
	 * @return Returns the parsed JSON document.
	 * @throws IOException When loading fails because of a network or other IO error.
	 * @throws JsonParseException When parsing of the received/loaded JSON document failed. This may for example happen if the server closes the connection after sending a successful response code.
	 */
	public static JsonObject load(URL url) throws IOException, JsonParseException {
		try (InputStream inputStream = url.openStream()) {
			return parse(new InputStreamReader(inputStream, defaultCharset), url.toString());
		}
	}

	private static final Charset defaultCharset = Charset.forName("utf-8");
}
