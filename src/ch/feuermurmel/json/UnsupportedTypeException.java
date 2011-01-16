package ch.feuermurmel.json;

public class UnsupportedTypeException extends RuntimeException {
	public UnsupportedTypeException(String message) {
		super(message);
	}

	public UnsupportedTypeException(String inputs, String type) {
		this("Only objects of type " + inputs + " can be converted to " + type + "!");
	}

	public UnsupportedTypeException(String message, RuntimeException e) {
		super(message, e);
	}
}
