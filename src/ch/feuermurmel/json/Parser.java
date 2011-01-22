package ch.feuermurmel.json;

import java.io.IOException;
import java.io.Reader;

final class Parser {
	public final JsonObject result;

	private final Lexer lexer;

	public Parser(Reader input) throws IOException {
		lexer = new Lexer(input);

		result = parse();

		if (!lexer.testToken(Lexer.TokenType.EndOfFile))
			throw problemUnexpectedToken("end of file");
	}

	private JsonParseException problemUnexpectedToken(String expected) throws IOException {
		Lexer.Token token = lexer.useToken();

		return new JsonParseException(token.line, token.column, "Expected " + expected);
	}

	private static String parseString(Lexer.Token token) {
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
					throw new JsonParseException(token.line, token.column + pos - 2, "Illegal string escape sequence");
				}
			} else {
				res.append(string.charAt(pos));
				pos += 1;
			}
		}

		return res.toString();
	}

	private JsonObject parse() throws IOException {
		if (lexer.testToken(Lexer.TokenType.Null)) {
			lexer.useToken();
			return new JsonNull();
		} else if (lexer.testToken(Lexer.TokenType.False)) {
			lexer.useToken();
			return new JsonBoolean(false);
		} else if (lexer.testToken(Lexer.TokenType.True)) {
			lexer.useToken();
			return new JsonBoolean(true);
		} else if (lexer.testToken(Lexer.TokenType.Integer)) {
			Lexer.Token token = lexer.useToken();
			
			try {
				return new JsonNumber(Long.valueOf(token.match));
			} catch (NumberFormatException ignored) {
				throw new JsonParseException(token.line, token.column, "Invalid number literal.");
			}
		} else if (lexer.testToken(Lexer.TokenType.Float)) {
			Lexer.Token token = lexer.useToken();

			try {
				return new JsonNumber(Double.valueOf(token.match));
			} catch (NumberFormatException ignored) {
				throw new JsonParseException(token.line, token.column, "Invalid number literal.");
			}
		} else if (lexer.testToken(Lexer.TokenType.String)) {
			return new JsonString(parseString(lexer.useToken()));
		} else if (lexer.testToken(Lexer.TokenType.OpenBracket)) {
			JsonList list = new JsonList();

			lexer.useToken(); // [

			if (!lexer.testToken(Lexer.TokenType.CloseBracket)) {
				while (true) {
					list.add(parse());

					if (lexer.testToken(Lexer.TokenType.Comma))
						lexer.useToken(); // ,
					else if (lexer.testToken(Lexer.TokenType.CloseBracket))
						break;
					else
						throw problemUnexpectedToken("comma or closing bracket");
				}
			}

			lexer.useToken(); // ]

			return list;
		} else if (lexer.testToken(Lexer.TokenType.OpenBrace)) {
			JsonMap map = new JsonMap();

			lexer.useToken(); // {

			if (!lexer.testToken(Lexer.TokenType.CloseBrace)) {
				while (true) {
					if (!lexer.testToken(Lexer.TokenType.String))
						throw problemUnexpectedToken("string");

					String key = parseString(lexer.useToken());

					if (!lexer.testToken(Lexer.TokenType.Colon))
						throw problemUnexpectedToken("colon");

					lexer.useToken(); // :
					map.add(key, parse());

					if (lexer.testToken(Lexer.TokenType.Comma))
						lexer.useToken(); // ,
					else if (lexer.testToken(Lexer.TokenType.CloseBrace))
						break;
					else
						throw problemUnexpectedToken("comma or closing brace");
				}
			}

			lexer.useToken(); // }

			return map;
		} else {
			throw problemUnexpectedToken("open brace or bracket, number, string or boolean literal or 'null'");
		}
	}

}
