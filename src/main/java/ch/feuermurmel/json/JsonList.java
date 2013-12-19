package ch.feuermurmel.json;

/**
 * Represents a JSON list.
 * <p/>
 * This list is mutable, elements can be added and removed at any index and the elements can be iterated over.
 * <p/>
 * Instances can be created using {@link Json#list()}. All JSON data structures also accept Java instances of {@link Iterable} as long as their elements are accepted by {@link Json#convert(Object)}.
 * <p/>
 * All methods change the value if this list support method-chaining.
 */
public interface JsonList extends JsonObject, Iterable<JsonObject> {
	/**
	 * Add an object at the end.
	 *
	 * @param e the object to add.
	 */
	JsonList add(Object e);

	/**
	 * Add an object at the specified index.
	 *
	 * @param index Index the object has after inserting.
	 * @param e The object to add.
	 */
	JsonList add(int index, Object e);

	/**
	 * Replace the object at the specified index.
	 *
	 * @param index Index of the object to replace.
	 * @param e The replacment object.
	 */
	JsonList set(int index, Object e);

	/**
	 * Remove the object at the specified index.
	 *
	 * @param index Index of object to remove.
	 */
	JsonList remove(int index);

	/**
	 * Get the object at the specified index.
	 *
	 * @param index Index of the element to return.
	 *
	 * @throws IndexOutOfBoundsException if the index is out of range.
	 */
	JsonObject get(int index);

	/**
	 * Same as {@link #get(int)} but returns a default value if the index is out of range.
	 *
	 * @param index Index of the element to return.
	 * @param def Value to return if the index is out of range.
	 */
	JsonObject get(int index, JsonObject def);

	/**
	 * Number of elements in this list.
	 */
	int size();

	/**
	 * Covariant overload of {@link JsonObject#clone()} so that the result does not need to be casted to a {@link JsonList}.
	 */
	@Override
	JsonList clone();
}
