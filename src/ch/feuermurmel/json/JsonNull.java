package ch.feuermurmel.json;

public final class JsonNull extends JsonObject {
	@Override
	public String toString() {
		return "null";
	}

	@Override
	public boolean equals(Object obj) {
		return obj.getClass() == JsonNull.class;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public JsonNull clone() {
		return this;
	}
}
