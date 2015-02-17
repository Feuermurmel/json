package ch.feuermurmel.json;

final class JsonDouble extends AbstractJsonNumber {
	private final double value;
	
	/**
	 * Create a JsonNumber using a double or float value.
	 *
	 * @param value Value of the new {@link AbstractJsonNumber}.
	 */
	JsonDouble(double value) {
		this.value = value;
	}
	
	@Override
	public boolean isFloating() {
		return true;
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
		if (obj instanceof JsonDouble) {
			return ((JsonDouble) obj).value == value;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return Double.valueOf(value).hashCode();
	}
}
