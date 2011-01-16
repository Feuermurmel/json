package ch.feuermurmel.json;

import java.util.ArrayList;

public abstract class PrettyPrint {
	private Format format = defaultFormat;
	
	private static final Format defaultFormat = new Format(7, "\t", "\n");

	protected abstract int numNodes();
	protected abstract void print(String indent, int numPrefix, Format format, StringBuilder builder);
	
	public final PrettyPrint format(int maxNodesPerLine, String lineIndent, String lineSepparator) {
		format = new Format(maxNodesPerLine, lineIndent, lineSepparator);
		return this;
	}
	
	public final PrettyPrint format(int maxNodesPerLine) {
		return format(maxNodesPerLine, format.lineIndent, format.lineSepparator);
	}
	
	public final PrettyPrint format(String lineIndent) {
		return format(format.maxNodesPerLine, lineIndent, format.lineSepparator);
	}
	
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
		private final String sep;
		private final String suffix;
		private final String prefixMulti;
		private final String sepMulti;
		private final String suffixMulti;

		public List(String prefix, String sep, String suffix, String prefixMulti, String sepMulti, String suffixMulti) {
			this.prefix = prefix;
			this.sep = sep;
			this.suffix = suffix;
			this.prefixMulti = prefixMulti;
			this.sepMulti = sepMulti;
			this.suffixMulti = suffixMulti;
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
				builder.append(prefixMulti);
				
				String indentPlus = indent + format.lineIndent;
				String sep2 = "";
				for (PrettyPrint i : elements) {
					builder.append(sep2 + format.lineSepparator + indentPlus);
					i.print(indentPlus, 0, format, builder);
					sep2 = sepMulti;
				}

				builder.append(format.lineSepparator + indent + suffixMulti);
			} else {
				builder.append(prefix);
				
				String sep2 = "";
				for (PrettyPrint i : elements) {
					builder.append(sep2);
					i.print("", 0, format, builder);
					sep2 = sep;
				}
				
				builder.append(suffix);
			}
		}
	}
}
