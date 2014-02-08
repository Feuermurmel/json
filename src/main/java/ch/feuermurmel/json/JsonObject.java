package ch.feuermurmel.json;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * Interface that is implemented by all JSON value types.
 * <p/>
 * Lists and maps have their own sub-interface, {@link JsonList} and {@link JsonMap}. The value of all other types can be accessed through this interface.
 * <p/>
 * The utility class {@link Json} defines methods to parse JSON documents and create JSON objects from Java data types.
 * <p/>
 * Instances of this class fulfill the contract on {@link Object#equals(Object)} and {@link Object#hashCode()}. Collections with the same content compare equal. Only values of the exact same type ever compare equal, numbers only compare equal if they're both integral or floating.
 */
public interface JsonObject extends JsonConvertible {
	/**
	 * Return whether this instance represents a JSON null.
	 */
	boolean isNull();

	/**
	 * Return whether this instance represents a JSON boolean.
	 * <p/>
	 * Use {@link #asBoolean()} to get its value.
	 */
	boolean isBoolean();

	/**
	 * Return whether this instance represents a JSON number.
	 * <p/>
	 * Use {@link #isIntegral()} and {@link #isFloating()} to test whether the number was specified as an integral or a floating number.
	 * <p/>
	 * You can use {@link #asFloat()} and {@link #asDouble()} to get the value of the number, which will work for integral as well as floating point numbers..
	 */
	boolean isNumber();

	/**
	 * Return whether this instance represents an JSON integral number.
	 * <p/>
	 * When a JSON file is parsed, only numbers that are specified with neither a decimal point nor an exponent are considered to be integral numbers.
	 * <p/>
	 * Use {@link #asInt()} and {@link #asLong()} to get the value of the number.
	 */
	boolean isIntegral();

	/**
	 * Return whether this instance represents a JSON floating number.
	 * <p/>
	 * When a JSON file is parsed, only numbers that are specified with a decimal point and/or an exponent are considered to be integral numbers.
	 * <p/>
	 * Use {@link #asFloat()} and {@link #asDouble()} to get the value of the number.
	 */
	boolean isFloating();

	/**
	 * Return whether this instance represents a JSON string.
	 * <p/>
	 * Use {@link #asString()} to get the value of the string.
	 */
	boolean isString();

	/**
	 * Return whether this instance represent a JSON list.
	 * <p/>
	 * Use {@link #asList()} to cast it to {@link JsonList} and access its contents.
	 */
	boolean isList();

	/**
	 * Return whether this instance represent a JSON map.
	 * <p/>
	 * Use {@link #asMap()} to cast it to {@link JsonMap} and access its contents.
	 */
	boolean isMap();

	/**
	 * Return the value of JSON boolean as a {@code boolean}.
	 *
	 * @throws UnsupportedTypeException when this object does not represent a JSON boolean as declared by {@link #isBoolean()}.
	 */
	boolean asBoolean();

	/**
	 * Return the value of an integral JSON number as an {@code int}.
	 * <p/>
	 * If the value is integral but its value is outside the range of an {@code int}, it will be truncated.
	 *
	 * @throws UnsupportedTypeException when this object does not represent an integral JSON number as declared by {@link #isIntegral()}.
	 */
	int asInt();

	/**
	 * Return the value of an integral JSON number as a {@code long}.
	 *
	 * @throws UnsupportedTypeException when this object does not represent an integral JSON number as declared by {@link #isIntegral()}.
	 */
	long asLong();

	/**
	 * Return the value of JSON number as a {@code float}. This may loose precision as the value internally stored as a {@code float} or {@code long}.
	 * <p/>
	 * This method can also be called on instance where {@link #isIntegral()} returns {@code true}.
	 *
	 * @throws UnsupportedTypeException when this object does not represent an JSON number as declared by {@link #isNumber()}.
	 */
	float asFloat();

	/**
	 * Return the value of JSON number as a {@code float}. This may loose precision of integral JSON number as they are internally stored as a {@code long}.
	 * <p/>
	 * This method can also be called on instance where {@link #isIntegral()} returns {@code true}.
	 *
	 * @throws UnsupportedTypeException when this object does not represent an JSON number as declared by {@link #isNumber()}.
	 */
	double asDouble();

	/**
	 * Return the value a JSON string a {@link String}.
	 *
	 * @throws UnsupportedTypeException when this object does not represent an JSON string as declared by {@link #isString()}.
	 */
	String asString();

	/**
	 * Cast this instance to a {@link JsonList}.
	 *
	 * @throws UnsupportedTypeException when this object does not represent an JSON list as declared by {@link #isList()}.
	 */
	JsonList asList();

	/**
	 * Create a {@link List} with the elements of this JSON list.
	 * <p/>
	 * The new list is independet of this JSON list but the elements themselves are not cloned.
	 */
	List<JsonObject> toList();

	/**
	 * Cast this instance to a {@link JsonMap}.
	 *
	 * @throws UnsupportedTypeException when this object does not represent an JSON map as declared by {@link #isMap()}.
	 */
	JsonMap asMap();

	/**
	 * Create a {@link Map} with the pairs of this JSON map.
	 * <p/>
	 * The new map is independet of this JSON map but the elements themselves are not cloned.
	 */
	Map<String, JsonObject> toMap();

	/**
	 * Return the JSON object in it's string representation.
	 * <p/>
	 * This will use a very compact representation with minimal whitespace. Use {@link #prettyPrint()} to get a wrapped and indented string representation.
	 */
	@Override
	String toString();

	/**
	 * Write this object as a JSON document.
	 *
	 * @param destination Instance of {@link Appendable}, like {@link StringBuilder} or {@link OutputStreamWriter}, to write the JSON document to.
	 *
	 * @throws IOException Thrown by the {@link Appendable}
	 */
	void toString(Appendable destination) throws IOException;

	/**
	 * Returns a {@link PrettyPrint} instance for this JsonObject which can be used to generate a string representation with more control over formatting.
	 * <p/>
	 * {@code PrettyPrint} implements {@code toString()}, an example usage might be:
	 * <p/>
	 * {@code System.out.println(obj.prettyPrint.format("\t", 12))}
	 */
	PrettyPrint prettyPrint();

	/**
	 * Return a JsonObject with same content as this one.
	 * <p/>
	 * Instances of mutable JSON lists and maps will be deep-cloned, while instances of other value types will just return themselves.
	 */
	JsonObject clone();
}
