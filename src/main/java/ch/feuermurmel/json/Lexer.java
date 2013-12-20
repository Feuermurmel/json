package ch.feuermurmel.json;

import java.io.*;
import java.util.regex.Pattern;

final class Lexer {
	private final Reader input;
	private final String sourceInfo;

	// line and column number of the character currently returned by useChar()
	private int currentLine = 1;
	private int currentColumn = 0;

	// contains a StringWriter to gather characters of a token, set to null while not parsing a token.
	private StringBuilder tokenWriter = null;

	// line and column number of the start of the currently recorded token.
	private int tokenLine = -1;
	private int tokenColumn = -1;

	// next token that will be returned from useToken() etc.
	private Token nextToken = null;

	// next character that will bre returned by useChar() etc. Set to -1 after the last character's been used.
	private int nextChar = -1;

	Lexer(Reader input, String sourceInfo) throws IOException, JsonParseException {
		this.input = input;
		this.sourceInfo = sourceInfo;

		nextChar = readChar();
		nextToken = readToken();
	}

	// read token and move to the next
	Token useToken() throws IOException, JsonParseException {
		Token res = nextToken;

		nextToken = readToken();

		return res;
	}

	// test the type of the current token
	boolean testToken(TokenType type) {
		return nextToken.type == type;
	}

	JsonParseException createParseException(String message) {
		return createParseException(currentLine, currentColumn, message);
	}

	JsonParseException createParseException(Token token, String message) {
		return createParseException(token.line, token.column, message);
	}

	JsonParseException createParseException(int line, int column, String message) {
		return JsonParseException.create(sourceInfo, line, column, message);
	}

	// parse one token including it's leading whitespace
	private Token readToken() throws IOException, JsonParseException {
		while (testChar(' ') || testChar('\t') || testChar('\n') || testChar('\r'))
			useChar();

		if (testChar(',')) {
			return singleCharToken(TokenType.comma);
		} else if (testChar(':')) {
			return singleCharToken(TokenType.colon);
		} else if (testChar('{')) {
			return singleCharToken(TokenType.openBrace);
		} else if (testChar('}')) {
			return singleCharToken(TokenType.closeBrace);
		} else if (testChar('[')) {
			return singleCharToken(TokenType.openBracket);
		} else if (testChar(']')) {
			return singleCharToken(TokenType.closeBracket);
		} else if (testChar('t')) {
			return keywordToken(TokenType.trueValue, "true");
		} else if (testChar('f')) {
			return keywordToken(TokenType.falseValue, "false");
		} else if (testChar('n')) {
			return keywordToken(TokenType.nullValue, "null");
		} else if (testChar('0', '9') || testChar('-')) {
			startToken();
			useChar();

			while (testChar('0', '9') || testChar('.') || testChar('e') || testChar('E') || testChar('-') || testChar('+'))
				useChar();
			
			// Cannot use the patters to find the match directly because we are reading from a Reader and not a CharSequence.
			if (integralPattern.matcher(getMatch()).matches())
				return finishToken(TokenType.integralValue);
			else if (floatingPattern.matcher(getMatch()).matches())
				return finishToken(TokenType.floatingValue);
			else
				throw createParseException("Invalid number");
		} else if (testChar('\"')) {
			// "((?:[^\\"]|\\.)*)"
			startToken();
			useChar();

			while (!testChar('\"')) {
				if (isEOF()) {
					String message = "EOF inside string";
					throw createParseException(message);
				}

				if (testControlChar())
					throw createParseException("Invalid control character");

				if (testChar('\\'))
					useChar();

				useChar();
			}

			useChar();

			return finishToken(TokenType.stringValue);
		} else if (isEOF()) {
			return new Token(TokenType.endOfFile, "", currentLine, currentColumn); // meaning there are no more tokens
		} else if (testControlChar()) {
			throw createParseException("Invalid control character");
		} else {
			throw createParseException("Invalid token");
		}
	}

	// record line and column number and start token recording
	private void startToken() {
		tokenWriter = new StringBuilder();
		tokenLine = currentLine;
		tokenColumn = currentColumn;
	}

	// end token recording and return the read string
	private Token finishToken(TokenType type) {
		String match = getMatch();

		tokenWriter = null;

		return new Token(type, match, tokenLine, tokenColumn);
	}

	// create a token form the single, current character, eats the character
	private Token singleCharToken(TokenType type) throws IOException {
		int line = currentLine;
		int column = currentColumn;

		return new Token(type, String.valueOf(useChar()), line, column);
	}

	// reads characters from a keyword token, and eats them. throws an exception if the keyword does not match.
	private Token keywordToken(TokenType type, String keyword) throws IOException, JsonParseException {
		int length = keyword.length();

		// we skip testing the first character as we wouldn't be here it it didn't match 
		useChar();

		for (int i = 1; i < length; i += 1) {
			if (!testChar(keyword.charAt(i)))
				throw createParseException(currentLine, currentColumn - i, "Invalid token"); // i hope we're safe with `currentColumn - i', multi-line keywords aren't that common ;-)

			useChar();
		}

		return new Token(type, keyword, currentLine, currentColumn - length);
	}

	private String getMatch() {
		return tokenWriter.toString();
	}

	// return the current character and move to the next
	private char useChar() throws IOException {
		char res = (char) nextChar;

		if (nextChar == '\n') {
			currentLine += 1;
			currentColumn = 0;
		} else {
			currentColumn += 1;
		}

		if (tokenWriter != null)
			tokenWriter.append(res);

		nextChar = readChar();

		return res;
	}

	// test whether the current character is in a given character range.
	private boolean testChar(char chMin, char chMax) {
		return nextChar >= chMin && nextChar <= chMax;
	}

	// test whether the current character is ch.
	private boolean testChar(char ch) {
		return nextChar == ch;
	}

	// test whether the current character is a control character.
	private boolean testControlChar() {
		return Character.isISOControl(nextChar);
	}

	// reads one character, return -1 on EOF
	private int readChar() throws IOException {
		return input.read();
	}

	private boolean isEOF() {
		return nextChar == -1;
	}

	public enum TokenType {
		openBrace,
		closeBrace,
		openBracket,
		closeBracket,
		colon,
		comma,
		stringValue,
		integralValue,
		floatingValue,
		trueValue,
		falseValue,
		nullValue,
		endOfFile
	}

	public static class Token {
		public final TokenType type;
		// complete token without whitespace
		public final String match;
		// line of the first character of the token, starting on line 1
		public final int line;
		// column of the first character of the token, starting in column 0
		public final int column;

		Token(TokenType type, String match, int line, int column) {
			this.type = type;
			this.match = match;
			this.line = line;
			this.column = column;
		}
	}

	private static final Pattern integralPattern = Pattern.compile("-?(0|[1-9][0-9]*)");
	private static final Pattern floatingPattern = Pattern.compile("(-?(0|[1-9][0-9]*))(\\.[0-9]+|(\\.[0-9]+)?[eE][+-]?[0-9]+)");
}
