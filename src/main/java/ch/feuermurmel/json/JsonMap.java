package ch.feuermurmel.json;

import java.io.IOException;
import java.util.*;

/**
 Represents a JSON map.
 <p/>
 This map is mutable, elements can be added and removed and the keys can be iterated over.
 <p/>
 Instances of this class can also be created using {@link Json#map()}. All JSON datastructures also accept Java instances of {@link Map} as long as thier values are accepted by {@link Json#convert(Object)}.
 <p/>
 All muting methods return the map instance to enable method-chaining.
 */
public final class JsonMap extends JsonObject implements Iterable<String> {
	private final Map<String, JsonObject> data = new LinkedHashMap<String, JsonObject>();

	private JsonMap() { }

	/**
	 Adds or replaces a value for the given key.
	 <p/>
	 May be used like this:
	 <p/>
	 {@code map.add("one", 1).add("two", 2);}

	 @param k The key. A String, may be empty but not null.
	 @param v The object to add.
	 */
	public JsonMap put(String k, Object v) {
		data.put(k, Json.convert(v));

		return this;
	}

	/**
	 Get the value for the specified key.

	 @param k Key that maps to the returned value.
	 @throws IllegalArgumentException if the key is not in the map.
	 */
	public JsonObject get(String k) {
		JsonObject res = data.get(k);

		if (res == null)
			throw new IllegalArgumentException("No such key in map: " + k);

		return res;
	}

	/**
	 Same as {@link #get(String)} but returns a default value if the key is not in the map.

	 @param k Key that maps to the returned value.
	 @param def Value to return if the key is not in the map.
	 */
	public JsonObject get(String k, JsonObject def) {
		JsonObject res = data.get(k);

		return res == null ? def : res;
	}

	/**
	 Remove a key-value pair form the map.

	 @param k Key of the pair to remove.
	 */
	public JsonMap remove(String k) {
		data.remove(k);

		return this;
	}

	/** Returns whether the map contains a mapping with the specified key. */
	public boolean has(String k) {
		return data.containsKey(k);
	}

	/** Number of key-value-pairs in the map. */
	public int size() {
		return data.size();
	}

	@Override
	public JsonMap asMap() {
		return this;
	}

	@Override
	public Iterator<String> iterator() {
		return data.keySet().iterator();
	}

	@Override
	public PrettyPrint prettyPrint() {
		PrettyPrint.List res = new PrettyPrint.List("{", "}", " ");

		for (Map.Entry<String, JsonObject> i : data.entrySet())
			res.add(new PrettyPrint.Prefix(JsonString.instance(i.getKey()) + ": ", i.getValue().prettyPrint()));

		return res;
	}

	@Override
	public void toString(Appendable dest) throws IOException {
		dest.append("{");

		String sep = "";
		for (Map.Entry<String, JsonObject> i : data.entrySet()) {
			dest.append(sep);
			JsonString.instance(i.getKey()).toString(dest);
			dest.append(":");
			i.getValue().toString(dest);

			sep = ",";
		}

		dest.append("}");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonMap.class)
			return false;

		return ((JsonMap) obj).data.equals(data);
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}

	@Override
	public JsonMap clone() {
		JsonMap res = new JsonMap();

		for (Map.Entry<String, JsonObject> i : data.entrySet())
			res.put(i.getKey(), i.getValue().clone());

		return res;
	}

	/** Create and return an empty {@code JsonMap}. */
	public static JsonMap create() {
		return new JsonMap();
	}

	/** Create and return a {@code JsonList} and initialize with the contents of {@code contents}. */
	// FIXME: Keys that are instances of JsonString will have quotes added before being used as keys in this JsonMap
	public static JsonMap create(Map<?, ?> content) {
		JsonMap map = new JsonMap();

		for (Map.Entry<?, ?> i : content.entrySet())
			map.put(i.getKey().toString(), i.getValue());

		return map;
	}
}