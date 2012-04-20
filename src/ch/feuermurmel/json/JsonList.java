package ch.feuermurmel.json;

import java.io.IOException;
import java.util.*;

/**
 Represents a JSON list.
 <p/>
 This list is mutable, elements can be added and removed at any index and the elements can be iterated over.
 <p/>
 Instances of this class can also be created using {@link Json#list()}. All JSON datastructures also accept Java instances of {@link Iterable} as long as thier elements are accepted by {@link Json#convert(Object)}.
 <p/>
 All muting methods return the list instance to enable method-chaining.
 */
public final class JsonList extends JsonObject implements Iterable<JsonObject> {
	private final List<JsonObject> data = new ArrayList<JsonObject>();

	private JsonList() { }

	/**
	 Add an object at the end.

	 @param e the object to add.
	 */
	public JsonList add(Object e) {
		data.add(Json.convert(e));

		return this;
	}

	/**
	 Add an object at the specified index.

	 @param index Index the object has after inserting.
	 @param e The object to add.
	 */
	public JsonList add(int index, Object e) {
		data.add(index, Json.convert(e));

		return this;
	}

	/**
	 Replace the object at the specified index.

	 @param index Index of the object to replace.
	 @param e The replacment object.
	 */
	public JsonList set(int index, Object e) {
		data.set(index, Json.convert(e));

		return this;
	}

	/**
	 Remove the object at the specified index.

	 @param index Index of object to remove.
	 */
	public JsonList remove(int index) {
		data.remove(index);

		return this;
	}

	/**
	 Get the object at the specified index.

	 @param index Index of the element to return.
	 @throws IndexOutOfBoundsException if the index is out of range.
	 */
	public JsonObject get(int index) {
		return data.get(index);
	}

	/**
	 Same as {@link #get(int)} but returns a default value if the index is out of range.

	 @param index Index of the element to return.
	 @param def Value to return if the index is out of range.
	 */
	public JsonObject get(int index, JsonObject def) {
		if (0 <= index && index < data.size())
			return data.get(index);
		else
			return def;
	}

	/** Number of elements in this list. */
	public int size() {
		return data.size();
	}

	@Override
	public JsonList asList() {
		return this;
	}

	@Override
	public Iterator<JsonObject> iterator() {
		return data.iterator();
	}

	@Override
	public PrettyPrint prettyPrint() {
		PrettyPrint.List res = new PrettyPrint.List("[", "]", "");

		for (JsonObject i : data)
			res.add(i.prettyPrint());

		return res;
	}

	@Override
	public void toString(Appendable dest) throws IOException {
		dest.append("[");

		String sep = "";
		for (JsonObject i : data) {
			dest.append(sep);
			i.toString(dest);

			sep = ",";
		}

		dest.append("]");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonList.class)
			return false;

		return ((JsonList) obj).data.equals(data);
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}

	@Override
	public JsonList clone() {
		JsonList res = new JsonList();

		for (JsonObject i : data)
			res.add(i.clone());

		return res;
	}

	/** Create and return an empty {@code JsonList}. */
	public static JsonList create() {
		return new JsonList();
	}

	/** Create and return a {@code JsonList} and initialize with the contents of {@code contents}. */
	public static JsonList create(Iterable<?> content) {
		JsonList list = new JsonList();

		for (Object i : content)
			list.add(i);

		return list;
	}
}
