package ch.feuermurmel.json;

/** Thrown when {@link Json#parse(String)} finds a syntax error in the passed JSON document. */
public final class JsonParseException extends Exception {
	JsonParseException(int line, int column, String msg) {
		super(String.format("At %d:%d: %s", line, column, msg));
	}
}
