package ch.feuermurmel.json;

import java.util.HashMap;
import java.util.Map;

/**
 Helper class for working with ch.feuermurmel.json.* instances and creating new instances.
 */
public final class Json {
	private Json() {
	}

	/**
	 Creates a new, empty JsonList.
	 This method is useful when used in a way like this:
	 <code>JsonList list = Json.list().add(1).add(2).add("three");</code>
	 @return a new, empty JsonList. 
	 */
	public static JsonList list() {
		return new JsonList();
	}
	
	/**
	 Creates a new, empty JsonMap.
	 This method is useful when used in a way like this:
	 <code>JsonMap map = Json.map().add("one", 1).add("two", 2);</code>
	 @return a new, empty JsonList. 
	 */
	public static JsonMap map() {
		return new JsonMap();
	}
	
	// Either cast or convert to JsonObject.
	public static JsonObject convert(Object obj) {
		// straight casting
		if (obj instanceof JsonObject)
			return (JsonObject) obj;
		
		// convertibles
		if (obj instanceof JsonConvertible)
			return ((JsonConvertible) obj).toJson();
		
		// special null treatment
		if (obj == null)
			return new JsonNull();
		
		// booleans
		if (obj instanceof Boolean)
			return new JsonBoolean((Boolean) obj);
		
		// numbers
		if (obj instanceof Byte)
			return new JsonNumber((Byte) obj);

		if (obj instanceof Short)
			return new JsonNumber((Short) obj);

		if (obj instanceof Integer)
			return new JsonNumber((Integer) obj);

		if (obj instanceof Long)
			return new JsonNumber((Long) obj);

		if (obj instanceof Float)
			return new JsonNumber((Float) obj);

		if (obj instanceof Double)
			return new JsonNumber((Double) obj);
		
		// strings
		if (obj instanceof Character)
			return new JsonString(obj.toString());

		if (obj instanceof String)
			return new JsonString((String) obj);
		
		// maps
		if (obj instanceof Map) {
			JsonMap res = new JsonMap();
			
			for (Map.Entry i : ((Map<?, ?>) obj).entrySet())
				res.add(i.getKey().toString(), i.getValue());
			
			return res;
		}
		
		// lists
		if (obj instanceof Iterable) {
			JsonList res = new JsonList();
			
			for (Object i : (Iterable) obj)
				res.add(i);
			
			return res;
		}

		throw new UnsupportedTypeException("Objects of type " + obj.getClass().getName() + " can't be converted to a JsonObject!");
	}
	
	public static JsonObject parse(String input) {
		return new Parser(input).result;
	}
}
