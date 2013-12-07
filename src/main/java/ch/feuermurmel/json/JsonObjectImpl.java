package ch.feuermurmel.json;

import java.io.IOException;

/**
 Super class of all classes implementing the different JSON datatypes.
 <p/>
 This class defines some convenience methods to convert JSON objects to Java datatypes and convert them to their string representation.
 <p/>
 The utility class {@link Json} defines methods to parse JSON documents and create JSON objects from Java datatypes.
 */
public abstract class JsonObjectImpl implements JsonObject {
	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();

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

	@Override
	@SuppressWarnings("DesignForExtension")
	public void toString(Appendable destination) throws IOException {
		destination.append(toString());
	}

	@Override
	@SuppressWarnings("DesignForExtension")
	public boolean asBoolean() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to boolean!");
	}

	@Override
	@SuppressWarnings("DesignForExtension")
	public long asLong() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to long!");
	}

	@Override
	public final int asInt() {
		return (int) asLong();
	}

	@Override
	@SuppressWarnings("DesignForExtension")
	public double asDouble() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to double!");
	}

	@Override
	public final float asFloat() {
		return (float) asDouble();
	}

	@Override
	@SuppressWarnings("DesignForExtension")
	public String asString() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a String!");
	}

	@Override
	@SuppressWarnings("DesignForExtension")
	public JsonListImpl asList() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a List!");
	}

	@Override
	@SuppressWarnings("DesignForExtension")
	public JsonMap asMap() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a Map!");
	}

	@SuppressWarnings("DesignForExtension")
	@Override
	public JsonObject clone() {
		return this;
	}

	@Override
	@SuppressWarnings("DesignForExtension")
	public PrettyPrint prettyPrint() {
		return new PrettyPrint.Atom(toString());
	}

	@Override
	public final JsonObject toJson() {
		return this;
	}
}
