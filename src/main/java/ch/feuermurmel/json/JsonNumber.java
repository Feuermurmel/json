package ch.feuermurmel.json;

/**
 Class used for representing JSON numbers.
 <p/>
 You do not need to create instances of this class when creating JSON data structures as all JSON classes accept Javas unboxed and boxed number types, where appropriate. You can also use {@link Json#convert(Object)} to create a JsonNumber.
 <p/>
 JsonNumbers can either contain an long or a double value. This can be tested using {@link #isIntegral()}. When parsing a number in a JSON document, numbers that either contain a decimal point or an exponent will result in a double value, while other number will result in an long value. When converting a JsonNumber to a string, the reverse convention will be used.
 */
public interface JsonNumber extends JsonObject {
	/**
	 Return whether this {@code JsonNumber} contains an integral value.

	 @returns {@code true} if the JSON number is internaly represented by a long, false if the JSON number is internaly represented by a double.
	 */
	boolean isIntegral();
}
