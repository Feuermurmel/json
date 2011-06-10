package ch.feuermurmel.json;

import java.io.*;
import java.util.List;
import java.util.Map;

/** Helper class for working with ch.feuermurmel.json.* instances and creating new instances. */
public final class Json {
	private Json() {
	}

	/**
	 Shortcut for {@link JsonList.create()}.
	 Returns a new, empty JsonList. Useful using a static import for this class. This method is useful when used in a way like this:
	 {@code JsonList list = list().add(1).add(2).add("three");}

	 @return a new, empty JsonList.
	 */
	public static JsonList list() {
		return JsonList.create();
	}

	/**
	 Shortcut for {@link JsonMap.create()}.
	 Returns a new, empty JsonMap. Useful using a static import for this class. This method is useful when used in a way like this:
	 <code>JsonMap map = map().add("one", 1).add("two", 2);</code>

	 @return a new, empty JsonList.
	 */
	public static JsonMap map() {
		return JsonMap.create();
	}

	/**
	 This method either casts or converts an object to a subclass of {@link JsonObject}.
	 <p/>
	 If the passed object is a subclass of JsonObject it will be cast. If the object implements {@link JsonConvertible}, it's {@link JsonConvertible#toJson()} method wil be called. Other types will be converted according to this list:
	 <p/>
	 - {@code null} will be converted to {@link JsonNull}.
	 <p/>
	 - {@code boolean} and instances of {@code Boolean} will be converted to {@link JsonBoolean}.
	 <p/>
	 - {@code byte}, {@code short}, {@code int}, {@code long}, {@code float}, {@code double} and instances of their boxed variants will be converted to {@link JsonNumber}.
	 <p/>
	 - {@code char} and instances of {@code Character} and {@code String} will be converted to {@link JsonString}.
	 <p/>
	 - Instances of {@link Map} will be converted to {@link JsonMap}. Their keys will be converted using {@code toString()}, their values using this method.
	 <p/>
	 - Instances of {@link Iterable} will be converted to {@link List}. Their elements will be converted using this method.

	 @param obj The object to convert.

	 @throws UnsupportedTypeException when the argument or any of it's members cannot be converted to a JsonObject.
	 */
	public static JsonObject convert(Object obj) {
		// convertibles
		if (obj instanceof JsonConvertible)
			return ((JsonConvertible) obj).toJson();

		// special null treatment
		if (obj == null)
			return JsonNull.instance();

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
	 Parse a JSON document into a {@link JsonObject}.
	 <p/>
	 Will parse the document according to the JSON document syntax. Can also be used on the output of {@link JsonObject#toString()} or {@link JsonObject#prettyPrint()}.

	 @param input The string to parse. May contain leading or trailing whitespace.

	 @throws JsonParseException if invalid syntax is encountered.
	 */
	public static JsonObject parse(String input) {
		try {
			return new Parser(new StringReader(input)).result;
		} catch (IOException e) {
			throw new RuntimeException(e); // should never happen as we're reading from a string
		}
	}

	/**
	 Parse a JSON document read from an inputStream.
	 <p/>
	 Currently, the stream has to end after the document or parsing will fail.
	 */
	public static JsonObject parse(InputStream input) throws IOException {
		return new Parser(new BufferedReader(new InputStreamReader(input))).result;
	}
}
