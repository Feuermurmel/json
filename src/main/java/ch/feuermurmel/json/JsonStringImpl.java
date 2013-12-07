package ch.feuermurmel.json;

import java.io.IOException;

final class JsonStringImpl extends JsonObjectImpl implements JsonString {
	private final String value;

	/**
	 Create an instance with the specified value.

	 @param value Value for the new {@code JsonString}.
	 */
	private JsonStringImpl(String value) {
		this.value = value;
	}

	@Override
	public String asString() {
		return value;
	}

	@Override
	public void toString(Appendable destination) throws IOException {
		destination.append("\"");

		for (int i = 0; i < value.length(); i += 1) {
			char c = value.charAt(i);

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

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonStringImpl.class)
			return false;

		return ((JsonStringImpl) obj).value.equals(value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public JsonString clone() {
		return this;
	}

	static JsonString instance(String value) {
		return new JsonStringImpl(value);
	}
}
