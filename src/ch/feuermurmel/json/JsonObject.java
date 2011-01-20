package ch.feuermurmel.json;

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
	public abstract boolean equals(Object obj);

	/**
	 Return the hash code of this JSON object.
	 <p/>
	 Overrides {@link Object#hashCode()}
	 */
	public abstract int hashCode();

	/**
	 Return a JsonObject with same content as this one.
	 <p/>
	 Instances of mutable JSON classes will be deep-cloned, while instances of immutable classes this will return their own instance.
	 <p/>
	 Overrides {@link Object#clone()}
	 */
	@Override
	public abstract JsonObject clone();

	/**
	 Return the JSON object in it's string representation.
	 <p/>
	 This will use a very compact representation with minimal whitespace. Use {@link #prettyPrint()} to get a wrapped and indented string representation.
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		toString(builder);
		
		return builder.toString();
	}
	
	protected void toString(StringBuilder builder) {
		builder.append(toString());
	}

	/**
	 Convert this JSON object to a Java boolean.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonBoolean}.
	 */
	public boolean asBoolean() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to boolean!");
	}

	/**
	 Convert this JSON object to a Java long.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonNumber}.
	 */
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
	public String asString() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a String!");
	}

	/**
	 Cast this JSON object to a {@link JsonString}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonString}.
	 */
	public JsonList asList() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a List!");
	}

	/**
	 Convert this JSON object to a {@link JsonMap}.

	 @throws UnsupportedTypeException when this object is not an instance of {@link JsonMap}.
	 */
	public JsonMap asMap() {
		throw new UnsupportedTypeException("Instances of " + getClass().getName() + " cannot be converted to a Map!");
	}

	/**
	 Returns a {@link PrettyPrint} instance for this JsonObject which can be used to generate a string representation with more control over formatting.
	 <p/>
	 {@code PrettyPrint} implements {@code toString()}, an example usage might be:
	 <p/>
	 {@code System.out.println(obj.prettyPrint.format("\t", 12))}
	 */
	public PrettyPrint prettyPrint() {
		return new PrettyPrint.Atom(toString());
	}

	@Override
	public final JsonObject toJson() {
		return this;
	}
}
