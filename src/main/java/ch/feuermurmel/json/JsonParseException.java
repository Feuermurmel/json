package ch.feuermurmel.json;

/**
 * Thrown when {@link Json#parse(String)} finds a syntax error in the passed JSON document.
 */
public final class JsonParseException extends Exception {
	public JsonParseException(String message) {
		super(message);
	}

	/**
	 * @param fileInfo May be null if no file name was specified.
	 * @param line
	 * @param column
	 * @param msg
	 *
	 * @return
	 */
	static JsonParseException create(String fileInfo, int line, int column, String msg) {
		String message;

		if (fileInfo == null)
			message = String.format("line %s, column %s: %s", line, column, msg);
		else
			message = String.format("%s: line %s, column %s: %s", fileInfo, line, column, msg);

		return new JsonParseException(message);
	}
}
