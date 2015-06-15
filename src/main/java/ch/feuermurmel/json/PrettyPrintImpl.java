package ch.feuermurmel.json;

import java.io.IOException;
import java.io.OutputStreamWriter;

final class PrettyPrintImpl implements PrettyPrint {
	private final PrettyPrintNode rootNode;
	private String lineIndent = "\t";
	private String lineSeparator = "\n";
	private long maxNodesPerLine = 7;
	
	PrettyPrintImpl(PrettyPrintNode rootNode) {
		this.rootNode = rootNode;
	}
	
	@Override
	public PrettyPrint setLineIndent(String lineIndent) {
		this.lineIndent = lineIndent;
		
		return this;
	}
	
	@Override
	public PrettyPrint setLineIndent(int numSpaces) {
		StringBuilder spacesBuilder = new StringBuilder();
		
		for (int i = 0; i < numSpaces; i += 1) {
			spacesBuilder.append(" ");
		}
		
		return setLineIndent(spacesBuilder.toString());
	}
	
	@Override
	public PrettyPrint setLineSeparator(String lineSeparator) {
		this.lineSeparator = lineSeparator;
		
		return this;
	}
	
	@Override
	public PrettyPrint setMaxTokensPerLine(long maxNodesPerLine) {
		this.maxNodesPerLine = maxNodesPerLine;
		
		return this;
	}
	
	/**
	 * Convert the formatted {@link JsonObject} to a string.
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		try {
			toString(builder);
		} catch (IOException e) {
			throw new AssertionError(e); // Should not happen, as we're writing to a StringBuilder
		}
		
		return builder.toString();
	}
	
	/**
	 * Write the formatted {@link JsonObject} to an instance of {@link Appendable} like {@link StringBuilder} or {@link OutputStreamWriter}.
	 */
	public void toString(Appendable destination) throws IOException {
		rootNode.toString(destination, "", lineIndent, lineSeparator, maxNodesPerLine);
	}
}
