package ch.feuermurmel.json;

/** Thrown when {@link Json#parse(String)} finds a syntax error in the passed JSON document. */
public final class JsonParseException extends Exception {
	/** @param msg Contains the line and column numbers and an explanation of the problem. */
	public JsonParseException(int line, int column, String msg) {
		super(String.format("At %d:%d: %s", line, column, msg));
	}
}
