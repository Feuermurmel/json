package ch.feuermurmel.json;

abstract class JsonNumberImpl extends JsonObjectImpl implements JsonNumber {
	@SuppressWarnings({ "FloatingPointEquality", "DesignForExtension" })
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JsonNumberImpl)
			return ((JsonObject) obj).asDouble() == asDouble();

		return false;
	}

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

	private static final class Long extends JsonNumberImpl {
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

	private static final class Double extends JsonNumberImpl {
		private final double value;

		/**
		 Create a JsonNumber using a double or float value.

		 @param value Value of the new {@link JsonNumberImpl}.
		 */
		public Double(double value) {
			this.value = value;
		}

		/** Return whether this JsonNumber contains an integral value. */
		@Override
		public boolean isIntegral() {
			return false;
		}

		/** Return the number casted to a long. This will result in truncation of double values. */
		@Override
		public long asLong() {
			return (long) value;
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
