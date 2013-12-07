package ch.feuermurmel.json;

import java.io.IOException;
import java.util.*;

final class JsonMapImpl extends JsonObjectImpl implements JsonMap {
	private final Map<String, JsonObject> data = new LinkedHashMap<String, JsonObject>();

	private JsonMapImpl() { }

	@Override
	public JsonMap put(String k, Object v) {
		data.put(k, Json.convert(v));

		return this;
	}

	@Override
	public JsonObject get(String k) {
		JsonObject res = data.get(k);

		if (res == null)
			throw new IllegalArgumentException("No such key in map: " + k);

		return res;
	}

	@Override
	public JsonObject get(String k, Object def) {
		JsonObject res = data.get(k);

		return res == null ? Json.convert(def) : res;
	}

	@Override
	public JsonMap remove(String k) {
		data.remove(k);

		return this;
	}

	@Override
	public boolean has(String k) {
		return data.containsKey(k);
	}

	@Override
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
			res.add(new PrettyPrint.Prefix(JsonStringImpl.instance(i.getKey()) + ": ", i.getValue().prettyPrint()));

		return res;
	}

	@Override
	public void toString(Appendable destination) throws IOException {
		destination.append("{");

		String sep = "";
		for (Map.Entry<String, JsonObject> i : data.entrySet()) {
			destination.append(sep);
			JsonStringImpl.instance(i.getKey()).toString(destination);
			destination.append(":");
			i.getValue().toString(destination);

			sep = ",";
		}

		destination.append("}");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonMapImpl.class)
			return false;

		return ((JsonMapImpl) obj).data.equals(data);
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}

	@Override
	public JsonMap clone() {
		JsonMap res = new JsonMapImpl();

		for (Map.Entry<String, JsonObject> i : data.entrySet())
			res.put(i.getKey(), i.getValue().clone());

		return res;
	}

	/** Create and return an empty {@code JsonMap}. */
	static JsonMap create() {
		return new JsonMapImpl();
	}

	/** Create and return a {@code JsonList} and initialize with the contents of {@code contents}. */
	// FIXME: Keys that are instances of JsonString will have quotes added before being used as keys in this JsonMap
	static JsonMap create(Map<?, ?> content) {
		JsonMap map = new JsonMapImpl();

		for (Map.Entry<?, ?> i : content.entrySet())
			map.put(i.getKey().toString(), i.getValue());

		return map;
	}
}
