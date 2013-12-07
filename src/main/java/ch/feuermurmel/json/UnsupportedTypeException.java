package ch.feuermurmel.json;

/** Thrown when one of {@link JsonObjectImpl}'s cast methods is called on an instance of the wrong subclass. */
public class UnsupportedTypeException extends RuntimeException {
	public UnsupportedTypeException(String message) {
		super(message);
	}

	public UnsupportedTypeException(String input, String type) {
		this(String.format("Only objects of type %s can be converted to %s!", input, type));
	}

	public UnsupportedTypeException(String message, RuntimeException e) {
		super(message, e);
	}
}
