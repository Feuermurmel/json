package ch.feuermurmel.json;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;

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
	 <p/>
	 Overrides {@link Object#equals(Object)}
	 */
	@Override
	public abstract boolean equals(Object obj);

	/**
	 Return the hash code of this JSON object.
	 <p/>
	 Overrides {@link Object#hashCode()}
	 */
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
	 Write this object as a JSON document to an instance of {@code Appendable} like {@link StringBuilder} or {@link OutputStreamWriter}.
	 */
	@SuppressWarnings("DesignForExtension")
	public void toString(Appendable dest) throws IOException {
		dest.append(toString());
	}

	// FIXME: `convert' is misleading
	/**
	 Convert this JSON object to a Java boolean.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonBoolean}.
	 */
	@SuppressWarnings("DesignForExtension")
	public boolean asBoolean() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to boolean!");
	}

	/**
	 Convert this JSON object to a Java long.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumber}.
	 */
	@SuppressWarnings("DesignForExtension")
	public long asLong() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to long!");
	}

	/**
	 Convert this JSON object to a Java int.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumber}.
	 */
	public final int asInt() {
		return (int) asLong();
	}

	/**
	 Convert this JSON object to a Java double.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumber}.
	 */
	@SuppressWarnings("DesignForExtension")
	public double asDouble() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to double!");
	}

	/**
	 Convert this JSON object to a Java float.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumber}.
	 */
	public final float asFloat() {
		return (float) asDouble();
	}

	/**
	 Convert this JSON object to a Java String.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonString}.
	 */
	@SuppressWarnings("DesignForExtension")
	public String asString() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a String!");
	}

	/**
	 Cast this JSON object to a {@link JsonString}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonString}.
	 */
	@SuppressWarnings("DesignForExtension")
	public JsonList asList() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a List!");
	}

	/**
	 Convert this JSON object to a {@link JsonMap}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonMap}.
	 */
	@SuppressWarnings("DesignForExtension")
	public JsonMap asMap() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a Map!");
	}

	/**
	 Return a JsonObject with same content as this one.
	 <p/>
	 Instances of mutable JSON classes will be deep-cloned, while instances of immutable classes this will return their own instance.
	 <p/>
	 Overrides {@link Object#clone()}
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
