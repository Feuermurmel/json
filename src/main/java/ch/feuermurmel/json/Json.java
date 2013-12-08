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

	// TODO: Overload this method with a vararg variant

	/**
	 * Shortcut for {@link JsonListImpl#create()}.
	 * <p/>
	 * Returns a new, empty {@link JsonListImpl}. Useful using a static import for this class. This method is useful when used in a way like this:
	 * <p/>
	 * {@code JsonList list = list().add(1).add(2).add("three");}
	 *
	 * @return a new, empty JsonList.
	 */
	public static JsonListImpl list() {
		return new JsonListImpl();
	}

	// TODO: Overload this method with a vararg variant

	/**
	 * Shortcut for {@link JsonMapImpl#create()}.
	 * <p/>
	 * Returns a new, empty {@link JsonMapImpl}. Useful using a static import for this class. This method is useful when used in a way like this:
	 * <p/>
	 * {@code JsonMap map = map().add("one", 1).add("two", 2);}
	 *
	 * @return a new, empty JsonList.
	 */
	public static JsonMap map() {
		return new JsonMapImpl();
	}

	// TODO: Overload this method with specific types

	/**
	 * This method either casts or converts an object to an implementation {@link JsonObject}.
	 * <p/>
	 * If the passed object is a subclass of JsonObject it will be cast. If the object implements {@link JsonConvertible}, it's {@link JsonConvertible#toJson()} method wil be called. Other types will be converted according to these rules:
	 * <p/>
	 * - {@code null} will be converted to {@link JsonNull}.
	 * <p/>
	 * - {@code boolean} and instances of {@code Boolean} will be converted to a JSON boolean.
	 * <p/>
	 * - {@code byte}, {@code short}, {@code int}, {@code long} and instances of their boxed variants will be converted to an integral JSON number.
	 * <p/>
	 * - {@code float}, {@code double} and instances of their boxed variants will be converted to a floating JSON number.
	 * <p/>
	 * - {@code char} and instances of {@code Character} and {@code String} will be converted to {@link JsonString}.
	 * <p/>
	 * - Instances of {@link Map} will be converted to a {@link JsonMap}. The values will be converted using this method. Only strings are allowed as keys.
	 * <p/>
	 * - Instances of {@link Iterable} will be converted to {@link List}. The elements will be converted using this method.
	 *
	 * @param obj The object to convert.
	 *
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
			return JsonBoolean.getInstance((Boolean) obj);

		// numbers
		if (obj instanceof Byte)
			return new JsonLong((Byte) obj);

		if (obj instanceof Short)
			return new JsonLong((Short) obj);

		if (obj instanceof Integer)
			return new JsonLong((Integer) obj);

		if (obj instanceof Long)
			return new JsonLong((Long) obj);

		if (obj instanceof Float)
			return new JsonDouble((Float) obj);

		if (obj instanceof Double)
			return new JsonDouble((Double) obj);

		// strings
		if (obj instanceof Character)
			return new JsonString(obj.toString());

		if (obj instanceof String)
			return new JsonString((String) obj);

		// maps
		if (obj instanceof Map) {
			JsonMap map = new JsonMapImpl();

			for (Map.Entry<?, ?> i : ((Map<?, ?>) obj).entrySet()) {
				Object key = i.getKey();

				if (!(key instanceof String))
					throw new UnsupportedTypeException(String.format("Objects of type %s can't be used as key in a JSON map.", i.getClass()));

				map.put((String) key, i.getValue());
			}

			return map;
		}

		// lists
		if (obj instanceof Iterable) {
			JsonListImpl list = new JsonListImpl();

			for (Object i : (Iterable<?>) obj)
				list.add(i);

			return list;
		}

		throw new UnsupportedTypeException(String.format("Objects of type %s can't be converted to a JsonObject.", obj.getClass().getName()));
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

	private static final Charset defaultCharset = Charset.forName("utf-8");
}
