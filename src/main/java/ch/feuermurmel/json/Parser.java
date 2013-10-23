package ch.feuermurmel.json;

import java.io.IOException;
import java.io.Reader;

final class Parser {
	private final Lexer lexer;

	private Parser(Lexer lexer) {
		this.lexer = lexer;
	}
	
	private String parseString(Lexer.Token token) throws JsonParseException {
		StringBuilder res = new StringBuilder();
		String string = token.match;
		int pos = 1; // skip leading "
		int length = string.length() - 1; // skip trailing "

		while (pos < length) {
			if (string.charAt(pos) == '\\') {
				char ch = string.charAt(pos + 1);
				pos += 2;

				if (ch == '"') {
					res.append('"');
				} else if (ch == '\\') {
					res.append('\\');
				} else if (ch == '/') {
					res.append('/');
				} else if (ch == 'b') {
					res.append('\b');
				} else if (ch == 'f') {
					res.append('\f');
				} else if (ch == 'n') {
					res.append('\n');
				} else if (ch == 'r') {
					res.append('\r');
				} else if (ch == 't') {
					res.append('\t');
				} else if (ch == 'u') {
					res.append((char) Integer.parseInt(string.substring(pos, pos + 4), 16));
					pos += 4;
				} else {
					throw lexer.createParseException(token.line, token.column + pos - 2, "Illegal string escape sequence");
				}
			} else {
				res.append(string.charAt(pos));
				pos += 1;
			}
		}

		return res.toString();
	}

	private JsonObject parse() throws IOException, JsonParseException {
		if (lexer.testToken(Lexer.TokenType.Null)) {
			lexer.useToken();
			return JsonNull.instance;
		} else if (lexer.testToken(Lexer.TokenType.False)) {
			lexer.useToken();
			return JsonBoolean.instance(false);
		} else if (lexer.testToken(Lexer.TokenType.True)) {
			lexer.useToken();
			return JsonBoolean.instance(true);
		} else if (lexer.testToken(Lexer.TokenType.Integer)) {
			Lexer.Token token = lexer.useToken();

			try {
				return JsonNumber.instance(Long.valueOf(token.match));
			} catch (NumberFormatException ignored) {
				throw lexer.createParseException(token, "Invalid number literal.");
			}
		} else if (lexer.testToken(Lexer.TokenType.Float)) {
			Lexer.Token token = lexer.useToken();

			try {
				return JsonNumber.instance(Double.valueOf(token.match));
			} catch (NumberFormatException ignored) {
				throw lexer.createParseException(token, "Invalid number literal.");
			}
		} else if (lexer.testToken(Lexer.TokenType.String)) {
			return JsonString.instance(parseString(lexer.useToken()));
		} else if (lexer.testToken(Lexer.TokenType.OpenBracket)) {
			JsonList list = JsonList.create();

			lexer.useToken(); // [

			if (!lexer.testToken(Lexer.TokenType.CloseBracket)) {
				while (true) {
					list.add(parse());

					if (lexer.testToken(Lexer.TokenType.Comma))
						lexer.useToken(); // ,
					else if (lexer.testToken(Lexer.TokenType.CloseBracket))
						break;
					else
						throw createParseExceptionUnexpectedToken("comma or closing bracket");
				}
			}

			lexer.useToken(); // ]

			return list;
		} else if (lexer.testToken(Lexer.TokenType.OpenBrace)) {
			JsonMap map = JsonMap.create();

			lexer.useToken(); // {

			if (!lexer.testToken(Lexer.TokenType.CloseBrace)) {
				while (true) {
					if (!lexer.testToken(Lexer.TokenType.String))
						throw createParseExceptionUnexpectedToken("string");

					String key = parseString(lexer.useToken());

					if (!lexer.testToken(Lexer.TokenType.Colon))
						throw createParseExceptionUnexpectedToken("colon");

					lexer.useToken(); // :
					map.put(key, parse());

					if (lexer.testToken(Lexer.TokenType.Comma))
						lexer.useToken(); // ,
					else if (lexer.testToken(Lexer.TokenType.CloseBrace))
						break;
					else
						throw createParseExceptionUnexpectedToken("comma or closing brace");
				}
			}

			lexer.useToken(); // }

			return map;
		} else {
			throw createParseExceptionUnexpectedToken("open brace or bracket, number, string or boolean literal or 'null'");
		}
	}
	
	private JsonParseException createParseExceptionUnexpectedToken(String expected) throws IOException, JsonParseException {
		return lexer.createParseException(lexer.useToken(), "Expected " + expected);
	}
	
	static JsonObject runParser(Reader input, String sourceInfo) throws IOException, JsonParseException {
		Lexer lexer = new Lexer(input, sourceInfo);
		Parser instance = new Parser(lexer);
		
		JsonObject result = instance.parse();
		
		if (!lexer.testToken(Lexer.TokenType.EndOfFile))
			throw instance.createParseExceptionUnexpectedToken("end of file");
		
		return result;
	}
}
