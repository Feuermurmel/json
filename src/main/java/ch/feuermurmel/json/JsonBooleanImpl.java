package ch.feuermurmel.json;

final class JsonBooleanImpl extends JsonObjectImpl implements JsonBoolean {
	private final boolean value;

	/**
	 Normally you use {@link Json#convert(Object)} to convert a Java boolean to a JSON object or directly use it as an argument with JSON data structures.

	 @param value value of the new JsonBoolean.
	 */
	private JsonBooleanImpl(boolean value) {
		this.value = value;
	}

	@Override
	public boolean asBoolean() {
		return value;
	}

	@Override
	public String toString() {
		return value ? "true" : "false";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonBooleanImpl.class)
			return false;

		return ((JsonBooleanImpl) obj).value == value;
	}

	@Override
	public int hashCode() {
		return Boolean.valueOf(value).hashCode();
	}

	@Override
	public JsonBoolean clone() {
		return this;
	}

	private static final JsonBoolean falseInstance = new JsonBooleanImpl(false);
	private static final JsonBoolean trueInstance = new JsonBooleanImpl(true);

	/**
	 Returns an instance of this calss with the specified boolean value.

	 @param value Boolean value to encapsulate.
	 */
	static JsonBoolean instance(boolean value) {
		return value ? trueInstance : falseInstance;
	}
}
