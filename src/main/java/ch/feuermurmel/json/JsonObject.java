package ch.feuermurmel.json;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 Super class of all classes implementing the different JSON datatypes.
 <p/>
 This class defines some convenience methods to convert JSON objects to Java datatypes and convert them to their string representation.
 <p/>
 The utility class {@link Json} defines methods to parse JSON documents and create JSON objects from Java datatypes.
 */
public abstract class JsonObject implements JsonConvertible, Cloneable {
	/**
	 Compare this JSON object to another JSON object.
	 <p/>
	 The passed object will not converted to a JSON object, so only an object of the same class will compare equal.
	 */
	@Override
	public abstract boolean equals(Object obj);

	/** Return the hash code of this JSON object. */
	@Override
	public abstract int hashCode();

	/**
	 Return the JSON object in it's string representation.
	 <p/>
	 This will use a very compact representation with minimal whitespace. Use {@link #prettyPrint()} to get a wrapped and indented string representation.
	 */
	@Override
	@SuppressWarnings("DesignForExtension")
	public String toString() {
		StringBuilder builder = new StringBuilder();

		try {
			toString(builder);
		} catch (IOException e) {
			throw new RuntimeException(e); // should never happen, as we're writing into a StringBuilder
		}

		return builder.toString();
	}

	/**
	 Write this object as a JSON document.

	 @param dest Instance of {@link Appendable}, like {@link StringBuilder} or {@link OutputStreamWriter}, to write the JSON document to.
	 @throws IOException Thrown by the {@link Appendable}
	 */
	@SuppressWarnings("DesignForExtension")
	public void toString(Appendable dest) throws IOException {
		dest.append(toString());
	}

	/**
	 Return the value of an instance of {@link JsonBoolean} as a {@code boolean}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonBoolean}.
	 */
	@SuppressWarnings("DesignForExtension")
	public boolean asBoolean() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a boolean!");
	}

	/**
	 Return the value of an instance of {@link JsonNumber} as a {@code long}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumber}.
	 */
	@SuppressWarnings("DesignForExtension")
	public long asLong() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a long!");
	}

	/**
	 Return the value of an instance of {@link JsonNumber} as an {@code int}. This will truncate values that are out of range.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumber}.
	 */
	public final int asInt() {
		return (int) asLong();
	}

	/**
	 Return the value of an instance of {@link JsonNumber} as a {@code double}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumber}.
	 */
	@SuppressWarnings("DesignForExtension")
	public double asDouble() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a double!");
	}

	/**
	 Return the value of an instance of {@link JsonNumber} as a {@code float}. This will truncate {@link JsonNumber} values that are out of range.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumber}.
	 */
	public final float asFloat() {
		return (float) asDouble();
	}

	/**
	 Return the value of an instance of {@link JsonString} as a {@link String}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonString}.
	 */
	@SuppressWarnings("DesignForExtension")
	public String asString() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a String!");
	}

	/**
	 Cast this JSON object to a {@link JsonList}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonString}.
	 */
	@SuppressWarnings("DesignForExtension")
	public JsonList asList() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a List!");
	}

	/**
	 Cast this JSON object to a {@link JsonMap}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonMap}.
	 */
	@SuppressWarnings("DesignForExtension")
	public JsonMap asMap() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a Map!");
	}

	/**
	 Return a JsonObject with same content as this one.
	 <p/>
	 Instances of mutable JSON classes will be deep-cloned, while instances of immutable classes this will return themselves instance.
	 */
	@SuppressWarnings("DesignForExtension")
	@Override
	public JsonObject clone() {
		return this;
	}

	/**
	 Returns a {@link PrettyPrint} instance for this JsonObject which can be used to generate a string representation with more control over formatting.
	 <p/>
	 {@code PrettyPrint} implements {@code toString()}, an example usage might be:
	 <p/>
	 {@code System.out.println(obj.prettyPrint.format("\t", 12))}
	 */
	@SuppressWarnings("DesignForExtension")
	public PrettyPrint prettyPrint() {
		return new PrettyPrint.Atom(toString());
	}

	@Override
	public final JsonObject toJson() {
		return this;
	}
}
