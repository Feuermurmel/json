package ch.feuermurmel.json;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class Parser {
	public final JsonObject result;
	public final String input;
	
	private final List<Token> tokens = new ArrayList<Token>();
	private int position = 0;

	public Parser(String input) {
		this.input = input;
		int pos = 0;
		int length = input.length();
		
		while (pos < length) {
			Token token = new Token(input, pos);

			tokens.add(token);
			pos = token.end;
		}
		
		result = parse();
		
		if (position < tokens.size())
			throw problemUnexpectedToken("end of file");
	}
	
	private JsonParseException problem(int position, String message) {
		int[] lineColumn = getLineColumn(position);
		
		return new JsonParseException("At " + lineColumn[0] + ":" + lineColumn[1] + ": " + message);
	}
	
	private JsonParseException problemUnexpectedToken(String expected) {
		Token tk = tokens.get(position);
		
		return problem(tk.start, "Unexpected token: '" + tk.match + "', expected " + expected + ".");
	}
	
	private int[] getLineColumn(int pos) {
		int line = 1;
		int column = 0;

		for (int i = 0; i < pos; i += 1) {
			if (input.charAt(i) == '\n') {
				line += 1;
				column = 0;
			} else {
				column += 1;
			}
		}
		
		return new int[] { line, column };
	}
	
	private String getToken() {
		String res = tokens.get(position).match;
		position += 1;
		return res;
	}
	
	private boolean testToken(TokenType type) {
		return tokens.get(position).type == type;
	}
	
	private void eatToken() {
		position += 1;
	}

	private JsonString parseString(String token) {
		StringBuilder builder = new StringBuilder();
		int pos = 0;
		int length = token.length();
		
		while (pos < length) {
			if (token.charAt(pos) == '\\') {
				char ch = token.charAt(pos + 1);
				pos += 2;
				
				if (ch == '"') {
					builder.append('"');
				} else if (ch == '\\') {
					builder.append('\\');
				} else if (ch == '/') {
					builder.append('/');
				} else if (ch == 'b') {
					builder.append('\b');
				} else if (ch == 'f') {
					builder.append('\f');
				} else if (ch == 'n') {
					builder.append('\n');
				} else if (ch == 'r') {
					builder.append('\r');
				} else if (ch == 't') {
					builder.append('\t');
				} else if (ch == 'u') {
					builder.append((char) Integer.parseInt(token.substring(pos, pos + 4), 16));
					pos += 4;
				} else {
					throw problem(tokens.get(position - 1).start + pos, "Illegal string escape: " + ch + ".");
				}
			} else {
				builder.append(token.charAt(pos));
				pos += 1;
			}
		}
		
		return new JsonString(builder.toString());
	}

	private JsonObject parse() {
		if (testToken(TokenType.Null)) {
			eatToken();
			return new JsonNull();
		} else if (testToken(TokenType.False)) {
			eatToken();
			return new JsonBoolean(false);
		} else if (testToken(TokenType.True)) {
			eatToken();
			return new JsonBoolean(true);
		} else if (testToken(TokenType.Integer)) {
			return new JsonNumber(Long.valueOf(getToken()));
		} else if (testToken(TokenType.Float)) {
			return new JsonNumber(Double.valueOf(getToken()));
		} else if (testToken(TokenType.String)) {
			return parseString(getToken());
		} else if (testToken(TokenType.OpenBracket)) {
			JsonList list = new JsonList();

			eatToken(); // [

			if (!testToken(TokenType.CloseBracket)) {
				while (true) {
					list.add(parse());

					if (testToken(TokenType.Comma))
						eatToken(); // ,
					else if (testToken(TokenType.CloseBracket))
						break;
					else
						throw problemUnexpectedToken("comma or closing bracket");
				}
			}
			
			eatToken(); // ]

			return list;
		} else if (testToken(TokenType.OpenBrace)) {
			JsonMap map = new JsonMap();

			eatToken(); // {

			if (!testToken(TokenType.CloseBrace)) {
				while (true) {
					if (!testToken(TokenType.String))
						throw problemUnexpectedToken("string");

					String key = parseString(getToken()).asString();

					if (!testToken(TokenType.Colon))
						throw problemUnexpectedToken("colon");

					eatToken(); // :
					map.add(key, parse());

					if (testToken(TokenType.Comma))
						eatToken(); // ,
					else if (testToken(TokenType.CloseBrace))
						break;
					else
						throw problemUnexpectedToken("comma or closing brace");
				}
			}

			eatToken(); // }

			return map;
		} else {
			throw problemUnexpectedToken("open brace or bracket, number, string or boolean literal or 'null'");
		}
	}

	@SuppressWarnings({ "InnerClassFieldHidesOuterClassField" })
	private class Token {
		public final String match;
		public final TokenType type;
		// start of the token
		public final int start;
		// start of the next token, i.e after whitespace
		public final int end;

		private Token(String input, int pos) {
			for (TokenType i : TokenType.values()) {
				Matcher matcher = i.pattern.matcher(input);
				boolean res = matcher.find(pos);
				
				if (res) {
					match = matcher.group(matcher.groupCount());
					type = i;
					start = matcher.start(1);
					end = matcher.end();
					
					return;
				}
			}
			
			throw problem(pos, "Invalid token: " + input.substring(pos, Math.min(input.length(), pos + 10)).trim() + " ...");
		}
	}
	
	private enum TokenType {
		OpenBrace("\\{"),
		CloseBrace("\\}"),
		OpenBracket("\\["),
		CloseBracket("\\]"),
		Colon(":"),
		Comma(","),
		String("\"((?:[^\\\\\"]|\\\\.)*)\""),
		Integer("(-?[0-9]+(?![.eE]))"),
		Float("(-?[0-9]+(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?)"),
		True("true"),
		False("false"),
		Null("null");
		
		public final Pattern pattern;
		
		TokenType(String pattern) {
			this.pattern = Pattern.compile("\\G[\\n\\t ]*(" + pattern + ")[\\n\\t ]*");
		}
	}
}
