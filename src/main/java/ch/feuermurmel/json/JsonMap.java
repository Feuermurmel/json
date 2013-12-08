package ch.feuermurmel.json;

import java.util.Map;

/**
 * Represents a JSON map.
 * <p/>
 * This map is mutable, elements can be added and removed and the keys can be iterated over.
 * <p/>
 * Instances can be created using {@link Json#map()}. All JSON datastructures also accept Java instances of {@link Map} as long as thier values are accepted by {@link Json#convert(Object)}.
 * <p/>
 * All methods change the value if this map support method-chaining.
 */
public interface JsonMap extends JsonObject, Iterable<String> {
	/**
	 * Adds or replaces a value for the given key.
	 * <p/>
	 * May be used like this:
	 * <p/>
	 * {@code map.add("one", 1).add("two", 2);}
	 *
	 * @param k The key. A String, may be empty but not null.
	 * @param v The object to add.
	 */
	JsonMap put(String k, Object v);

	/**
	 * Get the value for the specified key.
	 *
	 * @param k Key that maps to the returned value.
	 *
	 * @throws IllegalArgumentException if the key is not in the map.
	 */
	JsonObject get(String k);

	/**
	 * Same as {@link #get(String)} but returns a default value if the key is not in the map. The default value will be converted to a {@link AbstractJsonObject} using {@link Json#convert(Object)} automatically,
	 *
	 * @param k Key that maps to the returned value.
	 * @param def Value to return if the key is not in the map.
	 */
	JsonObject get(String k, Object def);

	/**
	 * Remove a key-value pair form the map.
	 *
	 * @param k Key of the pair to remove.
	 */
	JsonMap remove(String k);

	/**
	 * Returns whether the map contains a mapping with the specified key.
	 */
	boolean has(String k);

	/**
	 * Number of key-value-pairs in the map.
	 */
	int size();
}
