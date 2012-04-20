package ch.feuermurmel.json;

/** Thrown when one of {@link JsonObject}'s cast methods is called on an instance of the wrong subclass. */
public class UnsupportedTypeException extends RuntimeException {
	public UnsupportedTypeException(String message) {
		super(message);
	}

	public UnsupportedTypeException(String inputs, String type) {
		this(String.format("Only objects of type %s can be converted to %s!", inputs, type));
	}

	public UnsupportedTypeException(String message, RuntimeException e) {
		super(message, e);
	}
}
