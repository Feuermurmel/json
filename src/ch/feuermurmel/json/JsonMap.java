package ch.feuermurmel.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public final class JsonMap extends JsonObject implements Iterable<String> {
	private final Map<String, JsonObject> data = new LinkedHashMap<String, JsonObject>();

	/**
	 Adds or replaces a value for the given key.
	 May be used like this:
	 <code>map.add("one", 1).add("two", 2);</code>
	 @param k The key. A String, may be empty but not null.
	 @param v The value. May either by a JsonObject or any object that can be converted by Json.convert().
	 @return itself to allow method chaining.
	 */
	public JsonMap add(String k, Object v) {
		data.put(k, Json.convert(v));

		return this;
	}

	public JsonObject get(String k) {
		JsonObject res = data.get(k);
		
		if (res == null)
			throw new IllegalArgumentException("No such key in map: " + k);
		
		return res;
	}

	public JsonMap remove(String k) {
		data.remove(k);

		return this;
	}

	public boolean has(String k) {
		return data.containsKey(k);
	}
	
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
		PrettyPrint.List res = new PrettyPrint.List("{ ", ", ", " }", "{", ",", "}");
		
		for (Map.Entry<String, JsonObject> i : data.entrySet())
			res.add(new PrettyPrint.Prefix(new JsonString(i.getKey()) + ": ", i.getValue().prettyPrint()));
		
		return res;
	}

	@SuppressWarnings({ "StringConcatenationInsideStringBufferAppend" })
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("{ ");
		
		String sep = "";
		for (Map.Entry<String, JsonObject> i : data.entrySet()) {
			builder.append(sep + new JsonString(i.getKey()) + ": " + i.getValue());
			sep = ", ";
		}

		builder.append(" }");

		return builder.toString();
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
			res.add(i.getKey(), i.getValue().clone());

		return res;
	}
}
