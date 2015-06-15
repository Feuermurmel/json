package ch.feuermurmel.json;

/**
 * This class is used to pretty-print a JsonObject.
 * <p/>
 * {@link JsonObject#prettyPrint()} returns an instance of this class. {@link #toString()} will convert the original {@link JsonObject} to a string using the formatting parameters.
 */
public interface PrettyPrint {
	/**
	 * Set the string to add to the front of indented lines.
	 * <p/>
	 * Common choices are a tab or a bunch of spaces. Defaults to one tab.
	 */
	PrettyPrint setLineIndent(String lineIndent);
	
	/**
	 * Variant of {@link #setLineIndent(String)} which sets the indentation string to the specified number of spaces.
	 */
	PrettyPrint setLineIndent(int numSpaces);
	
	/**
	 * Set the string to use as line separator.
	 * <p/>
	 * Common choices are a newline or newline followed by carriage return character. Defaults to one newline character.
	 */
	PrettyPrint setLineSeparator(String lineSeparator);
	
	/**
	 * Set the number of tokens allowed inside a list or map before it is wrapped onto multiple lines. Defaults to {@code 7}.
	 * <p/>
	 * Each list, map and atomic value is counted as one node. Keys of map entries are also counted as a node. To decide whether to wrap the list or map on a line, only the tokens inside the list or map are counted.
	 */
	PrettyPrint setMaxTokensPerLine(long maxNodesPerLine);
}
