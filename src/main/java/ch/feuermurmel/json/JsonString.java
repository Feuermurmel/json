package ch.feuermurmel.json;

import java.io.IOException;

/**
 * Class used for representing JSON strings.
 * <p/>
 * You do not need to create instances of this class when creating JSON data structures as all JSON classes accept Javas true and false values, where appropriate. You can also use {@link Json#convert(Object)} to create a JsonString.
 * <p/>
 * Instances of {@code JsonString} are immutable like Java {@code Strings}.
 */
final class JsonString extends AbstractJsonObject {
	private final String value;

	@Override
	public boolean isString() {
		return true;
	}

	/**
	 * Create an instance with the specified value.
	 *
	 * @param value Value for the new {@code JsonString}.
	 */
	JsonString(String value) {
		this.value = value;
	}

	@Override
	public String asString() {
		return value;
	}

	@Override
	public void toString(Appendable destination) throws IOException {
		stringRepresentation(destination, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JsonString)
			return ((JsonString) obj).value.equals(value);

		return false;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	/**
	 * Write the representation of the specified {@link String} in a JSON document to the {@link Appendable}.
	 */
	static void stringRepresentation(Appendable destination, String string) throws IOException {
		destination.append("\"");

		for (int i = 0; i < string.length(); i += 1) {
			char c = string.charAt(i);

			if (c == '\"')
				destination.append("\\\"");
			else if (c == '\\')
				destination.append("\\\\");
			else if (c == '\n')
				destination.append("\\n");
			else if (c == '\t')
				destination.append("\\t");
			else if (c < (char) 32 || c > (char) 126)
				destination.append(String.format("\\u%04x", (int) c));
			else
				destination.append(c);
		}

		destination.append("\"");
	}
}
