package ch.feuermurmel.json;

import java.io.IOException;

/**
 * Super class of all classes implementing the different JSON data types.
 * <p/>
 * This class defines some convenience methods to convert JSON objects to Java data types and convert them to their string representation.
 * <p/>
 * The utility class {@link Json} defines methods to parse JSON documents and create JSON objects from Java data types.
 */
@SuppressWarnings("DesignForExtension")
abstract class AbstractJsonObject implements JsonObject {
	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		try {
			toString(builder);
		} catch (IOException e) {
			throw new RuntimeException(e); // should never happen, as we're writing into a StringBuilder
		}

		return builder.toString();
	}

	@Override
	public void toString(Appendable destination) throws IOException {
		destination.append(toString());
	}

	@Override
	public boolean asBoolean() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to boolean!");
	}

	@Override
	public long asLong() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to long!");
	}

	@Override
	public final int asInt() {
		return (int) asLong();
	}

	@Override
	public double asDouble() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to double!");
	}

	@Override
	public final float asFloat() {
		return (float) asDouble();
	}

	@Override
	public String asString() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a String!");
	}

	@Override
	public JsonList asList() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a List!");
	}

	@Override
	public JsonMap asMap() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a Map!");
	}

	@Override
	public JsonObject clone() {
		return this;
	}

	@Override
	public PrettyPrint prettyPrint() {
		return new PrettyPrint.Atom(toString());
	}

	@Override
	public final JsonObject toJson() {
		return this;
	}

	@Override
	public boolean isBoolean() {
		return false;
	}

	@Override
	public boolean isFloating() {
		return false;
	}

	@Override
	public boolean isIntegral() {
		return false;
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public boolean isMap() {
		return false;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public boolean isString() {
		return false;
	}
}
