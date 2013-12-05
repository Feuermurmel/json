//package ch.feuermurmel.json.binary;
//
//import ch.feuermurmel.json.*;
//import java.io.IOException;
//import java.nio.ByteBuffer;
//import java.util.*;
//
///**
// Represents a JSON list.
// <p/>
// This list is mutable, elements can be added and removed at any index and the elements can be iterated over.
// <p/>
// Instances of this class can also be created using {@link Json#list()}. All JSON datastructures also accept Java instances of {@link Iterable} as long as thier elements are accepted by {@link Json#convert(Object)}.
// <p/>
// All muting methods return the list instance to enable method-chaining.
// */
//public final class JsonBytes extends JsonObject {
//	private final ByteBuffer data;
//
//	private JsonBytes(ByteBuffer data) {
//		this.data = data;
//	}
//
//	/**
//	 Size of the bytes object in bytes.
//	 
//	 @return number of bytes in this JsonBytes object.
//	 */
//	public int size() {
//		return data.capacity();
//	}
//	
//	public ByteBuffer getBuffer() {
//		return data;
//	}
//
//	@Override
//	public PrettyPrint prettyPrint() {
//		PrettyPrint.List res = new PrettyPrint.List("[", "]", "");
//
//		for (JsonObject i : data)
//			res.add(i.prettyPrint());
//
//		return res;
//	}
//
//	@Override
//	public void toString(Appendable dest) throws IOException {
//		dest.append("[");
//
//		String sep = "";
//		for (JsonObject i : data) {
//			dest.append(sep);
//			i.toString(dest);
//
//			sep = ",";
//		}
//
//		dest.append("]");
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (obj.getClass() != JsonBytes.class)
//			return false;
//
//		return ((JsonBytes) obj).data.equals(data);
//	}
//
//	@Override
//	public int hashCode() {
//		return data.hashCode();
//	}
//
//	@Override
//	public JsonBytes clone() {
//		JsonBytes res = new JsonBytes(data);
//
//		for (JsonObject i : data)
//			res.add(i.clone());
//
//		return res;
//	}
//
//	/**
//	 Create and return an empty {@code JsonList}.
//	 */
//	public static JsonBytes create() {
//		return new JsonBytes(data);
//	}
//
//	/**
//	 Create and return a {@code JsonList} and initialize with the contents of {@code contents}.
//	 */
//	public static JsonBytes create(Iterable<?> content) {
//		JsonBytes list = new JsonBytes(data);
//		
//		for (Object i : content)
//			list.add(i);
//		
//		return list;
//	}
//}
