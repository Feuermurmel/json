package ch.feuermurmel.json;

/**
 Class used for representing JSON strings.
 <p/>
 You do not need to create instances of this class when creating JSON data structures as all JSON classes accept Javas true and false values, where appropriate. You can also use {@link Json#convert(Object)} to create a JsonString.
 <p/>
 Instances of {@code JsonString} are immutables like Java {@code Strings}.
 */
public final class JsonString extends JsonObject {
	private final String value;

	/**
	 Create an instance with the specified value.

	 @param value Value for the new {@code JsonString}.
	 */
	public JsonString(String value) {
		this.value = value;
	}

	@Override
	public String asString() {
		return value;
	}

	@Override
	public void toString(StringBuilder builder) {
		builder.append("\"");
		for (int i = 0; i < value.length(); i += 1) {
			char c = value.charAt(i);

			if (c == '\"')
				builder.append("\\\"");
			else if (c == '\\')
				builder.append("\\\\");
			else if (c == '\n')
				builder.append("\\n");
			else if (c == '\t')
				builder.append("\\t");
			else if (c < (char) 32 || c > (char) 126)
				builder.append(String.format("\\u%04x", (int) c));
			else
				builder.append(c);
		}

		builder.append("\"");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonString.class)
			return false;

		return ((JsonString) obj).value.equals(value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public JsonString clone() {
		return this;
	}
}
