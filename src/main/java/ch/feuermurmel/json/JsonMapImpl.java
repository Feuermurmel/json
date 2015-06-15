package ch.feuermurmel.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class JsonMapImpl extends AbstractJsonObject implements JsonMap {
	private final Map<String, JsonObject> data = new LinkedHashMap<>();
	
	@Override
	public boolean isMap() {
		return true;
	}
	
	@Override
	public JsonMap put(String k, Object v) {
		if (k == null) {
			throw new IllegalArgumentException("Key is null.");
		}
		
		data.put(k, Json.convert(v));
		
		return this;
	}
	
	@Override
	public JsonObject get(String k) {
		JsonObject res = data.get(k);
		
		if (res == null) {
			throw new IllegalArgumentException("No such key in map: " + k);
		}
		
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
	public Map<String, JsonObject> toMap() {
		return new LinkedHashMap<>(data);
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
		if (obj instanceof JsonMapImpl) {
			return ((JsonMapImpl) obj).data.equals(data);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return data.hashCode();
	}
	
	@Override
	public JsonMap clone() {
		JsonMap res = new JsonMapImpl();
		
		for (Map.Entry<String, JsonObject> i : data.entrySet()) {
			res.put(i.getKey(), i.getValue().clone());
		}
		
		return res;
	}
	
	@Override
	protected PrettyPrintNode createPrettyPrintNode() {
		List<PrettyPrintNode> childNodes = data
			.entrySet()
			.stream()
			.map(JsonMapImpl::createNodeFromEntry)
			.collect(Collectors.toList());
		
		return new PrettyPrintNode.Sequence(childNodes, prettyPrintSyntaxElements);
	}
	
	private static final PrettyPrintNode.Sequence.SyntaxElements prettyPrintSyntaxElements = new PrettyPrintNode.Sequence.SyntaxElements("{ ", " }", "{", "}");
	
	private static PrettyPrintNode createNodeFromEntry(Map.Entry<String, JsonObject> entry) {
		StringBuilder keyBuilder = new StringBuilder();
		JsonObject value = entry.getValue();
		
		try {
			JsonString.stringRepresentation(keyBuilder, entry.getKey());
		} catch (IOException exception) {
			// Can't happen with a StringBuilder.
			throw new AssertionError(exception);
		}
		
		keyBuilder.append(": ");
		
		if (!(value instanceof AbstractJsonObject)) {
			throw new IllegalStateException(String.format("Value of unknown type %s cannot be pretty-printed.", value.getClass()));
		}
		
		return new PrettyPrintNode.Prefix(keyBuilder.toString(), ((AbstractJsonObject) value).createPrettyPrintNode());
	}
}
