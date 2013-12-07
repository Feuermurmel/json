package ch.feuermurmel.json;

final class JsonNullImpl extends JsonObjectImpl implements JsonNull {
	private JsonNullImpl() { }

	@Override
	public String toString() {
		return "null";
	}

	@Override
	public boolean equals(Object obj) {
		return obj.getClass() == JsonNullImpl.class;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public JsonNull clone() {
		return this;
	}

	static final JsonNull instance = new JsonNullImpl();
}
