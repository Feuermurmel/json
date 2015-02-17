package ch.feuermurmel.json;

/**
 * An interface for classes which support automatic conversion to JSON objects.
 * <p/>
 * Use {@link #toJson()} to convert an object directly to a JsonObject or use the object as arguments in a JSON data structure which will call the method automatically.
 * <p/>
 * Implement this interface make enable class being used as arguments to methods like {@link JsonListImpl#add(Object)} and {@link JsonMapImpl#put(String, Object)}.
 */
public interface JsonConvertible {
	/**
	 * Convert an object of a class implementing this interface to a {@link AbstractJsonObject}.
	 */
	JsonObject toJson();
}
