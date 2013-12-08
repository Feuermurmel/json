package ch.feuermurmel.json;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * This class is used to pretty-print a JsonObject.
 * <p/>
 * {@link AbstractJsonObject#prettyPrint()} returns an instance of this class. The different {@code format(...)} methods can be used to specify line complexity before wrapping occurs and the string used for indenting.
 * <p/>
 * {@link #toString()} will convert the original {@link JsonObject} to a string using the set formatting parameters.
 */
public abstract class PrettyPrint {
	private Format format = defaultFormat;

	protected abstract int numNodes();

	protected abstract boolean isSimple();

	protected abstract void toString(String indent, int numPrefix, Format format, Appendable destination) throws IOException;

	/**
	 * Set formatting parameters used for pretty-printing.
	 *
	 * @param maxNodesPerLine Maximum number of tokes on a line before line wrapping occurs.
	 * @param lineIndent String used to indent lines inside data structures.
	 * @param lineSeparator String used to separate lines.
	 */
	public final PrettyPrint format(int maxNodesPerLine, String lineIndent, String lineSeparator) {
		format = new Format(maxNodesPerLine, lineIndent, lineSeparator);
		return this;
	}

	/**
	 * @see #format(int, String, String)
	 */
	public final PrettyPrint format(int maxNodesPerLine) {
		return format(maxNodesPerLine, format.lineIndent, format.lineSeparator);
	}

	/**
	 * @see #format(int, String, String)
	 */
	public final PrettyPrint format(String lineIndent) {
		return format(format.maxNodesPerLine, lineIndent, format.lineSeparator);
	}

	/**
	 * @see #format(int, String, String)
	 */
	public final PrettyPrint format(String lineIndent, String lineSeparator) {
		return format(format.maxNodesPerLine, lineIndent, lineSeparator);
	}

	/**
	 * Convert the formatted {@link JsonObject} to a string.
	 */
	@Override
	public final String toString() {
		StringBuilder builder = new StringBuilder();

		try {
			toString(builder);
		} catch (IOException e) {
			throw new RuntimeException(e); // Should not happen, as we're writing to a StringBuilder
		}

		return builder.toString();
	}

	/**
	 * Write the formatted {@link JsonObject} to an instance of {@link Appendable} like {@link StringBuilder} or {@link OutputStreamWriter}.
	 */
	public final void toString(Appendable destination) throws IOException {
		toString("", 0, format, destination);
	}

	private static final Format defaultFormat = new Format(7, "\t", "\n");

	private static final class Format {
		public final int maxNodesPerLine;
		public final String lineIndent;
		public final String lineSeparator;

		private Format(int maxNodesPerLine, String lineIndent, String lineSeparator) {
			this.maxNodesPerLine = maxNodesPerLine;
			this.lineIndent = lineIndent;
			this.lineSeparator = lineSeparator;
		}
	}
	
	static final class Atom extends PrettyPrint {
		private final String value;

		Atom(String value) {
			this.value = value;
		}

		@Override
		protected int numNodes() {
			return 1;
		}

		@Override
		protected boolean isSimple() {
			return true;
		}

		@Override
		protected void toString(String indent, int numPrefix, Format format, Appendable destination) throws IOException {
			destination.append(value);
		}
	}

	static final class Prefix extends PrettyPrint {
		private final String prefix;
		private final PrettyPrint value;

		Prefix(String prefix, PrettyPrint value) {
			this.prefix = prefix;
			this.value = value;
		}

		@Override
		protected int numNodes() {
			return 1 + value.numNodes();
		}

		@Override
		protected boolean isSimple() {
			return value.isSimple();
		}

		@Override
		protected void toString(String indent, int numPrefix, Format format, Appendable destination) throws IOException {
			destination.append(prefix);
			value.toString(indent, numPrefix + 1, format, destination);
		}
	}

	static final class List extends PrettyPrint {
		private final java.util.List<PrettyPrint> elements = new ArrayList<>();
		private final String prefix;
		private final String suffix;
		private final String umfixWS;

		List(String prefix, String suffix, String umfixWS) {
			this.prefix = prefix;
			this.suffix = suffix;
			this.umfixWS = umfixWS;
		}

		public void add(PrettyPrint elem) {
			elements.add(elem);
		}

		@Override
		protected int numNodes() {
			int res = 1;

			for (PrettyPrint i : elements)
				res += i.numNodes();

			return res;
		}

		@Override
		protected boolean isSimple() {
			return elements.isEmpty();
		}

		@Override
		protected void toString(String indent, int numPrefix, Format format, Appendable destination) throws IOException {
			boolean simple = true;

			for (PrettyPrint i : elements) {
				if (!i.isSimple()) {
					simple = false;
					break;
				}
			}

			if (elements.isEmpty()) {
				destination.append(prefix + umfixWS + suffix);
			} else if (simple || numNodes() + numPrefix <= format.maxNodesPerLine) {
				destination.append(prefix + umfixWS);

				String sep = "";
				for (PrettyPrint i : elements) {
					destination.append(sep);
					i.toString("", 0, format, destination);
					sep = ", ";
				}

				destination.append(umfixWS + suffix);
			} else {
				destination.append(prefix);

				String indentPlus = indent + format.lineIndent;
				String sep = "";
				for (PrettyPrint i : elements) {
					destination.append(sep + format.lineSeparator + indentPlus);
					i.toString(indentPlus, 0, format, destination);
					sep = ",";
				}

				destination.append(format.lineSeparator + indent + suffix);
			}
		}
	}
}
