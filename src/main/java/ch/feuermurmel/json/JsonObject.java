package ch.feuermurmel.json;

import java.io.IOException;
import java.io.OutputStreamWriter;

public interface JsonObject extends JsonConvertible {
	/**
	 Return the value of an instance of {@link JsonBooleanImpl} as a {@code boolean}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonBooleanImpl}.
	 */
	boolean asBoolean();

	/**
	 Return the value of an instance of {@link JsonNumberImpl} as a {@code long}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumberImpl}.
	 */
	long asLong();

	/**
	 Return the value of an instance of {@link JsonNumberImpl} as an {@code int}. This will truncate values that are out of range.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumberImpl}.
	 */
	int asInt();

	/**
	 Return the value of an instance of {@link JsonNumberImpl} as a {@code double}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumberImpl}.
	 */
	double asDouble();

	/**
	 Return the value of an instance of {@link JsonNumberImpl} as a {@code float}. This will truncate {@link JsonNumberImpl} values that are out of range.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumberImpl}.
	 */
	float asFloat();

	/**
	 Return the value of an instance of {@link JsonStringImpl} as a {@link String}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonStringImpl}.
	 */
	String asString();

	/**
	 Cast this JSON object to a {@link JsonListImpl}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonStringImpl}.
	 */
	JsonListImpl asList();

	/**
	 Cast this JSON object to a {@link JsonMapImpl}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonMapImpl}.
	 */
	JsonMap asMap();

	/**
	 Return the JSON object in it's string representation.
	 <p/>
	 This will use a very compact representation with minimal whitespace. Use {@link #prettyPrint()} to get a wrapped and indented string representation.
	 */
	@Override
	String toString();

	/**
	 Write this object as a JSON document.

	 @param destination Instance of {@link Appendable}, like {@link StringBuilder} or {@link OutputStreamWriter}, to write the JSON document to.
	 @throws IOException Thrown by the {@link Appendable}
	 */
	void toString(Appendable destination) throws IOException;

	/**
	 Returns a {@link PrettyPrint} instance for this JsonObject which can be used to generate a string representation with more control over formatting.
	 <p/>
	 {@code PrettyPrint} implements {@code toString()}, an example usage might be:
	 <p/>
	 {@code System.out.println(obj.prettyPrint.format("\t", 12))}
	 */
	PrettyPrint prettyPrint();

	/**
	 Return a JsonObject with same content as this one.
	 <p/>
	 Instances of mutable JSON classes will be deep-cloned, while instances of immutable classes this will return themselves instance.
	 */
	JsonObject clone();
}
