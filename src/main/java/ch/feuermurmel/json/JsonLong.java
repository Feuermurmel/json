package ch.feuermurmel.json;

final class JsonLong extends AbstractJsonNumber {
	private final long value;

	JsonLong(long value) {
		this.value = value;
	}

	@Override
	public boolean isIntegral() {
		return true;
	}

	/**
	 * Return the number casted to a long. This will result in truncation of double values.
	 */
	@Override
	public long asLong() {
		return value;
	}

	/**
	 * Return the number casted to a double.
	 */
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
		if (obj instanceof JsonLong)
			return ((JsonLong) obj).value == value;

		return false;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(value).hashCode();
	}
}
