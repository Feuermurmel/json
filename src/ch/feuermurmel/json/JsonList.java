package ch.feuermurmel.json;

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
	 */
	public JsonObject get(int index) {
		return data.get(index);
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
		PrettyPrint.List res = new PrettyPrint.List("[", ", ", "]", "[", ",", "]");

		for (JsonObject i : data)
			res.add(i.prettyPrint());

		return res;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("[");

		String sep = "";
		for (JsonObject i : data) {
			builder.append(sep);
			builder.append(i);

			sep = ", ";
		}

		builder.append("]");

		return builder.toString();
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
}
