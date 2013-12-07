package ch.feuermurmel.json;

import java.io.IOException;
import java.util.*;

final class JsonListImpl extends JsonObjectImpl implements JsonList {
	private final List<JsonObject> data = new ArrayList<>();

	private JsonListImpl() { }

	@Override
	public JsonListImpl add(Object e) {
		data.add(Json.convert(e));

		return this;
	}

	@Override
	public JsonListImpl add(int index, Object e) {
		data.add(index, Json.convert(e));

		return this;
	}

	@Override
	public JsonListImpl set(int index, Object e) {
		data.set(index, Json.convert(e));

		return this;
	}

	@Override
	public JsonListImpl remove(int index) {
		data.remove(index);

		return this;
	}

	@Override
	public JsonObject get(int index) {
		return data.get(index);
	}

	@Override
	public JsonObject get(int index, JsonObject def) {
		if (0 <= index && index < data.size())
			return data.get(index);
		else
			return def;
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public JsonListImpl asList() {
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
	public void toString(Appendable destination) throws IOException {
		destination.append("[");

		String sep = "";
		for (JsonObject i : data) {
			destination.append(sep);
			i.toString(destination);

			sep = ",";
		}

		destination.append("]");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonListImpl.class)
			return false;

		return ((JsonListImpl) obj).data.equals(data);
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}

	@Override
	public JsonListImpl clone() {
		JsonListImpl res = new JsonListImpl();

		for (JsonObject i : data)
			res.add(i.clone());

		return res;
	}

	/** Create and return an empty {@code JsonList}. */
	static JsonListImpl create() {
		return new JsonListImpl();
	}

	/**
	 Create and return a {@code JsonList} and initialize with the contents of the specified sequence.
	 <p/>
	 Each value will be converted by {@link Json#convert(Object)}

	 @param content Sequence to use the values from.
	 */
	static JsonListImpl create(Iterable<?> content) {
		JsonListImpl list = new JsonListImpl();

		for (Object i : content)
			list.add(i);

		return list;
	}
}
