package ch.feuermurmel.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

final class JsonListImpl extends AbstractJsonObject implements JsonList {
	private final List<JsonObject> data = new ArrayList<>();
	
	@Override
	public boolean isList() {
		return true;
	}
	
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
		if (0 <= index && index < data.size()) {
			return data.get(index);
		} else {
			return def;
		}
	}
	
	@Override
	public int size() {
		return data.size();
	}
	
	@Override
	public List<JsonObject> toList() {
		return new ArrayList<>(data);
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
		if (obj instanceof JsonListImpl) {
			return ((JsonListImpl) obj).data.equals(data);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return data.hashCode();
	}
	
	@Override
	public JsonList clone() {
		JsonListImpl res = new JsonListImpl();
		
		for (JsonObject i : data) {
			res.add(i.clone());
		}
		
		return res;
	}
	
	@Override
	protected PrettyPrintNode createPrettyPrintNode() {
		List<PrettyPrintNode> childNodes = data
			.stream()
			.map(JsonListImpl::createNodeFromElement)
			.collect(Collectors.toList());
		
		return new PrettyPrintNode.Sequence(childNodes, prettyPrintSyntaxElements);
	}
	
	private static final PrettyPrintNode.Sequence.SyntaxElements prettyPrintSyntaxElements = new PrettyPrintNode.Sequence.SyntaxElements("[", "]", "[", "]");
	
	private static PrettyPrintNode createNodeFromElement(JsonObject element) {
		if (!(element instanceof AbstractJsonObject)) {
			throw new IllegalStateException(String.format("Value of unknown type %s cannot be pretty-printed.", element.getClass()));
		}
		
		return ((AbstractJsonObject) element).createPrettyPrintNode();
	}
}
