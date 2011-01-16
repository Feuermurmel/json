package ch.feuermurmel.json;

import java.io.*;
import java.util.*;

public final class JsonList extends JsonObject implements Iterable<JsonObject> {
	private final List<JsonObject> data = new ArrayList<JsonObject>();

	public JsonList add(Object e) {
		data.add(Json.convert(e));
		
		return this;
	}

	public JsonList add(int index, Object e) {
		data.add(index, Json.convert(e));

		return this;
	}

	public JsonList remove(int index) {
		data.remove(index);

		return this;
	}

	public JsonObject get(int index) {
		return data.get(index);
	}
	
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
