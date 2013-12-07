package ch.feuermurmel.json;

/**
 Class used for representing JSON strings.
 <p/>
 You do not need to create instances of this class when creating JSON data structures as all JSON classes accept Javas true and false values, where appropriate. You can also use {@link Json#convert(Object)} to create a JsonString.
 <p/>
 Instances of {@code JsonString} are immutables like Java {@code Strings}.
 */
public interface JsonString extends JsonObject {
}
