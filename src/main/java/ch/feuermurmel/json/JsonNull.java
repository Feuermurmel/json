package ch.feuermurmel.json;

/**
 * Class with only one instance representing the JSON 'null' value.
 * <p/>
 * This class us used so null values extracted from a JSON data structure need no special reatment for method calls like .equals() or .clone(). You do not need to create instances of this class as all JSON classes accept Java nulls as arguments, where appropriate.
 */
final class JsonNull extends AbstractJsonObject {
	private JsonNull() {
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public String toString() {
		return "null";
	}

	@Override
	public boolean equals(Object obj) {
		return obj == instance;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	static final JsonObject instance = new JsonNull();
}
