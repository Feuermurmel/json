package ch.feuermurmel.json;

/**
 Class used for representing JSON numbers.
 <p/>
 You do not need to create instances of this class when creating JSON data structures as all JSON classes accept Javas unboxed and boxed number types, where appropriate. You can also use {@link Json#convert(Object)} to create a JsonNumber.
 <p/>
 JsonNumbers can either contain an long or a double value. This can be tested using {@link #isIntegral()}. When parsing a number in a JSON document, numbers that either contain a decimal point or an exponent will result in a double value, while other number will result in an long value. When converting a JsonNumber to a string, the reverse convention will be used.
 */
public abstract class JsonNumber extends JsonObject {
	@SuppressWarnings({ "FloatingPointEquality", "DesignForExtension" })
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JsonNumber)
			return ((JsonObject) obj).asDouble() == asDouble();

		return false;
	}

	/**
	 Return whether this {@code JsonNumber} contains an integral value.

	 @returns {@code true} if the JSON number is internaly represented by a long, false if the JSON number is internaly represented by a double.
	 */
	public abstract boolean isIntegral();

	/**
	 Create a JsonNumber using an integral value.

	 @param value Value of the new {@code JsonNumber}.
	 */
	static JsonNumber instance(long value) {
		return new Long(value);
	}

	/**
	 Create a JsonNumber using an integral value.

	 @param value Value of the new {@code JsonNumber}.
	 */
	static JsonNumber instance(double value) {
		return new Double(value);
	}

	private static final class Long extends JsonNumber {
		private final long value;

		private Long(long value) {
			this.value = value;
		}

		@Override
		public boolean isIntegral() {
			return true;
		}

		/** Return the number casted to a long. This will result in truncation of double values. */
		@Override
		public long asLong() {
			return value;
		}

		/** Return the number casted to a double. */
		@Override
		public double asDouble() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		@SuppressWarnings("FloatingPointEquality")
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Long)
				return ((Long) obj).value == value;

			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			return java.lang.Long.valueOf(value).hashCode();
		}
	}

	private static final class Double extends JsonNumber {
		private final double value;

		/**
		 Create a JsonNumber using a double or float value.

		 @param value Value of the new {@link JsonNumber}.
		 */
		public Double(double value) {
			this.value = value;
		}

		/** Return whether this JsonNumber contains an integral value. */
		@Override
		public boolean isIntegral() {
			return false;
		}
		
		/** Return the number casted to a double. */
		@Override
		public double asDouble() {
			return value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

		@SuppressWarnings("FloatingPointEquality")
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Double)
				return ((Double) obj).value == value;

			return super.equals(obj);
		}

		@Override
		public int hashCode() {
			return java.lang.Double.valueOf(value).hashCode();
		}
	}
}
