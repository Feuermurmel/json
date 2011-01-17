package ch.feuermurmel.json;

/**
 An interface for classes which support automatic conversion to JSON objects.
 <p/>
 Use {@link #toJson()} to convert an object directly to a JsonObject or use the object as arguments in a JSON datastructure which will call the method automatically.
 <p/>
 Implement this interace make enable class being used as arguments to methods like {@link JsonList#add(Object)} and {@link JsonMap#add(String, Object)}.
 */
public interface JsonConvertible {
	/** Convert an object of a class implementing this interface to a {@link JsonObject}. */
	JsonObject toJson();
}
