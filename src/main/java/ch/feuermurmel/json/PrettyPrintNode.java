package ch.feuermurmel.json;

import java.io.IOException;
import java.util.List;

abstract class PrettyPrintNode {
	final long numTokens;
	
	protected PrettyPrintNode(long numTokens) {
		this.numTokens = numTokens;
	}
	
	/**
	 * This method should not add any indent of its own to the destination before also adding a line separator.
	 */
	abstract void toString(Appendable destination, String currentIndent, String lineIndent, String lineSeparator, long maxTokensPerLine) throws IOException;
	
	static final class Atom extends PrettyPrintNode {
		final String value;
		
		Atom(String value) {
			super(0);
			
			this.value = value;
		}
		
		@Override
		void toString(Appendable destination, String currentIndent, String lineIndent, String lineSeparator, long maxTokensPerLine) throws IOException {
			destination.append(value);
		}
	}
	
	static final class Prefix extends PrettyPrintNode {
		final String prefix;
		final PrettyPrintNode node;
		
		Prefix(String prefix, PrettyPrintNode node) {
			super(1 + node.numTokens);
			
			this.prefix = prefix;
			this.node = node;
		}
		
		@Override
		void toString(Appendable destination, String currentIndent, String lineIndent, String lineSeparator, long maxTokensPerLine) throws IOException {
			destination.append(prefix);
			
			node.toString(destination, currentIndent, lineIndent, lineSeparator, maxTokensPerLine);
		}
	}
	
	static final class Sequence extends PrettyPrintNode {
		final List<PrettyPrintNode> childNodes;
		final SyntaxElements syntaxElements;
		
		Sequence(List<PrettyPrintNode> childNodes, SyntaxElements syntaxElements) {
			// Calculate the number of nodes _inside_ this sequence. As sequences do not count themselves and atoms have a count of 0, 1 can be added to each element.
			super(childNodes.stream().map(x -> x.numTokens + 1).reduce(0l, (x, y) -> x + y));
			
			this.childNodes = childNodes;
			this.syntaxElements = syntaxElements;
		}
		
		@Override
		void toString(Appendable destination, String currentIndent, String lineIndent, String lineSeparator, long maxTokensPerLine) throws IOException {
			boolean wrap = numTokens > maxTokensPerLine;
			String childIndent;
			String elementSeparator;
			
			if (wrap) {
				childIndent = currentIndent + lineIndent;
				elementSeparator = elementSeparatorWrapped + lineSeparator + childIndent;
				
				destination.append(syntaxElements.startWrapped);
				destination.append(lineSeparator);
				destination.append(childIndent);
			} else {
				childIndent = currentIndent;
				elementSeparator = elementSeparatorNonWrapped;
				
				destination.append(syntaxElements.startNonWrapped);
			}
			
			String sep = "";
			
			for (PrettyPrintNode i : childNodes) {
				destination.append(sep);
				
				i.toString(destination, childIndent, lineIndent, lineSeparator, maxTokensPerLine);
				
				sep = elementSeparator;
			}
			
			if (wrap) {
				destination.append(lineSeparator + currentIndent);
				destination.append(syntaxElements.endWrapped);
			} else {
				destination.append(syntaxElements.endNonWrapped);
			}
		}
		
		static final class SyntaxElements {
			final String startNonWrapped;
			final String endNonWrapped;
			final String startWrapped;
			final String endWrapped;
			
			SyntaxElements(String startNonWrapped, String endNonWrapped, String startWrapped, String endWrapped) {
				this.startNonWrapped = startNonWrapped;
				this.endNonWrapped = endNonWrapped;
				this.startWrapped = startWrapped;
				this.endWrapped = endWrapped;
			}
		}
		
		private static final String elementSeparatorNonWrapped = ", ";
		private static final String elementSeparatorWrapped = ",";
	}
}
