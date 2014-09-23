package ch.feuermurmel.json;

/**
 * Thrown when one of {@link JsonObject}'s cast methods is called on an instance of the wrong subclass or {@link Json#convert(Object)} is used to convert a Java value that cannot be converted to a JSON value.
 */
public class UnsupportedTypeException extends RuntimeException {
	UnsupportedTypeException(String message) {
		super(message);
	}
}
