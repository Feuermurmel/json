package ch.feuermurmel.json;

/** Thrown when {@link Json#parse(String)} finds a syntax error in the passed JSON document. */
public class JsonParseException extends RuntimeException {
	/** @param msg Contains the line and column numbers and an explanation of the problem. */
	public JsonParseException(int line, int column, String msg) {
		super(String.format("At %d:%d: %s", line, column, msg));
	}
}
