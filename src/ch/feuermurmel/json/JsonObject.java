package ch.feuermurmel.json;

import java.io.IOException;
import java.io.OutputStream;

@SuppressWarnings({ "override" })
public abstract class JsonObject implements JsonConvertible, Cloneable {
	public abstract boolean equals(Object obj);
	public abstract int hashCode();
	public abstract JsonObject clone();
	public abstract String toString();
	
	public boolean asBoolean() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to boolean!");
	}

	public long asLong() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to long!");
	}

	public final int asInt() {
		return (int) asLong();
	}

	public double asDouble() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to double!");
	}

	public final float asFloat() {
		return (float) asDouble();
	}

	public String asString() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a String!");
	}

	public JsonList asList() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a List!");
	}

	public JsonMap asMap() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a Map!");
	}
	
	public PrettyPrint prettyPrint() {
		return new PrettyPrint.Atom(toString());
	}

	@Override
	public final JsonObject toJson() {
		return this;
	}
}
