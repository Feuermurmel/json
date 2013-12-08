package ch.feuermurmel.json;

import java.io.IOException;
import java.util.*;

final class JsonMapImpl extends AbstractJsonObject implements JsonMap {
	private final Map<String, JsonObject> data = new LinkedHashMap<>();

	@Override
	public boolean isMap() {
		return true;
	}

	@Override
	public JsonMap put(String k, Object v) {
		if (k == null)
			throw new IllegalArgumentException("Key is null.");

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

		for (Map.Entry<String, JsonObject> i : data.entrySet()) {
			StringBuilder builder = new StringBuilder();

			try {
				JsonString.stringRepresentation(builder, i.getKey());
			} catch (IOException e) {
				// Can't happen with a StringBuilder.
				throw new AssertionError(e);
			}

			builder.append(":");

			res.add(new PrettyPrint.Prefix(builder.toString(), i.getValue().prettyPrint()));
		}

		return res;
	}

	@Override
	public void toString(Appendable destination) throws IOException {
		destination.append("{");

		String sep = "";
		for (Map.Entry<String, JsonObject> i : data.entrySet()) {
			destination.append(sep);
			JsonString.stringRepresentation(destination, i.getKey());
			destination.append(":");
			i.getValue().toString(destination);

			sep = ",";
		}

		destination.append("}");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JsonMapImpl)
			return ((JsonMapImpl) obj).data.equals(data);

		return false;
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
}
