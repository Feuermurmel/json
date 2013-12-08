package ch.feuermurmel.json;

/**
 * Class used for representing JSON boolean values.
 * <p/>
 * You do not need to create instances of this class when creating JSON data structures as all JSON classes accept Javas true and false values, where appropriate. You can also use Json.convert(b) to create a JsonBoolean.
 */
final class JsonBoolean extends AbstractJsonObject {
	private final boolean value;

	private JsonBoolean(boolean value) {
		this.value = value;
	}

	@Override
	public boolean isBoolean() {
		return true;
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
		if (obj instanceof JsonBoolean)
			return ((JsonBoolean) obj).value == value;

		return false;
	}

	@Override
	public int hashCode() {
		return Boolean.valueOf(value).hashCode();
	}

	static final JsonObject falseInstance = new JsonBoolean(false);
	static final JsonObject trueInstance = new JsonBoolean(true);
}
