package ch.feuermurmel.json;

/**
 Class used for representing JSON boolean values.
 <p/>
 You do not need to create instances of this class when creating JSON data structures as all JSON classes accept Javas true and false values, where appropriate. You can also use Json.convert(b) to create a JsonBoolean.
 */
public final class JsonBoolean extends JsonObject {
	private final boolean value;

	@Override
	public boolean asBoolean() {
		return value;
	}

	/**
	 Normally you use {@link Json#convert(Object)} to convert a Java boolean to a JSON object or directly use it as an argument with JSON data structures.

	 @param value value of the new JsonBoolean.
	 */
	public JsonBoolean(boolean value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value ? "true" : "false";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonBoolean.class)
			return false;

		return ((JsonBoolean) obj).value == value;
	}

	@Override
	public int hashCode() {
		return Boolean.valueOf(value).hashCode();
	}

	@Override
	public JsonBoolean clone() {
		return this;
	}
}
