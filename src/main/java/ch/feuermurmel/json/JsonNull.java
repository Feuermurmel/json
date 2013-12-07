package ch.feuermurmel.json;

/**
 Class with only one instance representing the JSON 'null' value.
 <p/>
 This class us used so null values extracted from a JSON data structure need no special reatment for method calls like .equals() or .clone(). You do not need to create instances of this class as all JSON classes accept Java nulls as arguments, where appropriate.
 */
public interface JsonNull extends JsonObject {
}
