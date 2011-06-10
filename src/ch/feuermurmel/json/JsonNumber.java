package ch.feuermurmel.json;

/**
 Class used for representing JSON numbers.
 <p/>
 You do not need to create instances of this class when creating JSON data structures as all JSON classes accept Javas unboxed and boxed number types, where appropriate. You can also use {@link Json#convert(Object)} to create a JsonNumber.
 <p/>
 JsonNumbers can either contain an long or a double value. This can be tested using {@link #isInteger()}. When parsing a number in a JSON document, numbers that either contain a decimal point or an exponent will result in a double value, while other number will result in an long value. When converting a JsonNumber to a string, the reverse convention will be used.
 */
public final class JsonNumber extends JsonObject {
	private final long valueLong;
	private final double valueDouble;

	/**
	 Create a JsonNumber using a double or float value.

	 @param value Value of the new {@link JsonNumber}.
	 */
	private JsonNumber(double value) {
		valueLong = 0;
		valueDouble = value;
	}

	/**
	 Create a JsonNumber using an integral value.

	 @param value Value of the new {@link JsonNumber}.
	 */
	private JsonNumber(long value) {
		valueLong = value;
		valueDouble = 0d;
	}

	/** Return whether this JsonNumber contains an integral value. */
	public boolean isInteger() {
		return valueDouble == 0d;
	}

	/** Return the number casted to a long. This will result in truncation of double values. */
	@Override
	public long asLong() {
		if (isInteger())
			return valueLong;
		else
			return (long) valueDouble;
	}

	/** Return the number casted to a double. */
	@Override
	public double asDouble() {
		if (isInteger())
			return valueLong;
		else
			return valueDouble;
	}

	@Override
	public String toString() {
		if (isInteger())
			return String.valueOf(valueLong);
		else
			return String.valueOf(valueDouble);
	}

	@SuppressWarnings({ "FloatingPointEquality" })
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonNumber.class)
			return false;

		if (valueDouble == 0d)
			return ((JsonNumber) obj).valueLong == valueLong;
		else
			return ((JsonNumber) obj).valueDouble == valueDouble;
	}

	@Override
	public int hashCode() {
		if (isInteger())
			return Long.valueOf(valueLong).hashCode();
		else
			return Double.valueOf(valueDouble).hashCode();
	}

	@Override
	public JsonObject clone() {
		return this;
	}

	public static JsonNumber instance(long value) {
		return new JsonNumber(value);
	}

	public static JsonNumber instance(double value) {
		return new JsonNumber(value);
	}
}
