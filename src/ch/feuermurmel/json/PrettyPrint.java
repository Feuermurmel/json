package ch.feuermurmel.json;

import java.util.ArrayList;

/**
 This class is used to pretty-print a JsonObject.
 <p/>
 {@link JsonObject#prettyPrint()} returns an instance of this class. The different {@code format} methods can be used to specify line complexity before wrapping occrus and the string used for indenting.
 <p/>
 {@link #toString()} will convert the original {@code JsonObject} to a string using the set formating parameters.
 */
@SuppressWarnings({ "JavaDoc" })
public abstract class PrettyPrint {
	private Format format = defaultFormat;

	private static final Format defaultFormat = new Format(7, "\t", "\n");

	protected abstract int numNodes();

	protected abstract void print(String indent, int numPrefix, Format format, StringBuilder builder);

	/**
	 Set formatting parameters used for pretty-printing.

	 @param maxNodesPerLine Maximum number of tokes on a line before line wrapping occurs.
	 @param lineIndent String used to indent lines inside data structures.
	 @param lineSepparator String used to sepparate lines.
	 */
	public final PrettyPrint format(int maxNodesPerLine, String lineIndent, String lineSepparator) {
		format = new Format(maxNodesPerLine, lineIndent, lineSepparator);
		return this;
	}

	/** @see #format(int, String, String) */
	public final PrettyPrint format(int maxNodesPerLine) {
		return format(maxNodesPerLine, format.lineIndent, format.lineSepparator);
	}

	/** @see #format(int, String, String) */
	public final PrettyPrint format(String lineIndent) {
		return format(format.maxNodesPerLine, lineIndent, format.lineSepparator);
	}

	/** @see #format(int, String, String) */
	public final PrettyPrint format(String lineIndent, String lineSepparator) {
		return format(format.maxNodesPerLine, lineIndent, lineSepparator);
	}

	public final String toString() {
		StringBuilder builder = new StringBuilder();

		print("", 0, format, builder);

		return builder.toString();
	}

	private static final class Format {
		public final int maxNodesPerLine;
		public final String lineIndent;
		public final String lineSepparator;

		private Format(int maxNodesPerLine, String lineIndent, String lineSepparator) {
			this.maxNodesPerLine = maxNodesPerLine;
			this.lineIndent = lineIndent;
			this.lineSepparator = lineSepparator;
		}
	}

	public static final class Atom extends PrettyPrint {
		private final String value;

		public Atom(String value) {
			this.value = value;
		}

		@Override
		protected int numNodes() {
			return 1;
		}

		@Override
		protected void print(String indent, int numPrefix, Format format, StringBuilder builder) {
			builder.append(value);
		}
	}

	public static final class Prefix extends PrettyPrint {
		private final String prefix;
		private final PrettyPrint value;

		public Prefix(String prefix, PrettyPrint value) {
			this.prefix = prefix;
			this.value = value;
		}

		@Override
		protected int numNodes() {
			return 1 + value.numNodes();
		}

		@Override
		protected void print(String indent, int numPrefix, Format format, StringBuilder builder) {
			builder.append(prefix);
			value.print(indent, numPrefix + 1, format, builder);
		}
	}

	public static final class List extends PrettyPrint {
		private final java.util.List<PrettyPrint> elements = new ArrayList<PrettyPrint>();
		private final String prefix;
		private final String suffix;
		private final String umfixWS;

		public List(String prefix, String suffix, String umfixWS) {
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
		protected void print(String indent, int numPrefix, Format format, StringBuilder builder) {
			if (numNodes() + numPrefix > format.maxNodesPerLine) {
				builder.append(prefix);

				String indentPlus = indent + format.lineIndent;
				String sep = "";
				for (PrettyPrint i : elements) {
					builder.append(sep + format.lineSepparator + indentPlus);
					i.print(indentPlus, 0, format, builder);
					sep = ",";
				}

				builder.append(format.lineSepparator + indent + suffix);
			} else if (elements.isEmpty()) {
				builder.append(prefix + umfixWS + suffix);
			} else {
				builder.append(prefix + umfixWS);

				String sep = "";
				for (PrettyPrint i : elements) {
					builder.append(sep);
					i.print("", 0, format, builder);
					sep = ", ";
				}

				builder.append(umfixWS + suffix);
			}
		}
	}
}
