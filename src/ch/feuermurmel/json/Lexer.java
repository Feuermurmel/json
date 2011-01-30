package ch.feuermurmel.json;

import java.io.*;

final class Lexer {
	private final Reader input;

	// line and column number of the character currently returned by useChar()
	private int currentLine = 1;
	private int currentColumn = 0;

	// contains a StringWriter to gather characters of a token, set to null while not parsing a token.
	private StringWriter tokenWriter = null;

	// line and column number of the start of the currently recorded token.
	private int tokenLine = -1;
	private int tokenColumn = -1;

	// next token that will be returned from useToken() etc.
	private Token nextToken = null;

	// next character that will bre returned by useChar() etc. Set to -1 after the last character's been used.
	private int nextChar = -1;

	public Lexer(Reader input) throws IOException {
		this.input = input;

		nextChar = readChar();
		nextToken = readToken();
	}

	// read token and move to the next
	public Token useToken() throws IOException {
		Token res = nextToken;
		
		nextToken = readToken();
		
		return res;
	}

	// test the type of the current token
	public boolean testToken(TokenType type) {
		return nextToken.type == type;
	}

	// parse one token including it's leading whitespace
	private Token readToken() throws IOException {
		while (testChar(' ') || testChar('\t') || testChar('\n'))
			useChar();

		if (testChar(',')) {
			return singleCharToken(TokenType.Comma);
		} else if (testChar(':')) {
			return singleCharToken(TokenType.Colon);
		} else if (testChar('{')) {
			return singleCharToken(TokenType.OpenBrace);
		} else if (testChar('}')) {
			return singleCharToken(TokenType.CloseBrace);
		} else if (testChar('[')) {
			return singleCharToken(TokenType.OpenBracket);
		} else if (testChar(']')) {
			return singleCharToken(TokenType.CloseBracket);
		} else if (testChar('t')) {
			return keywordToken(TokenType.True, "true");
		} else if (testChar('f')) {
			return keywordToken(TokenType.False, "false");
		} else if (testChar('n')) {
			return keywordToken(TokenType.Null, "null");
		} else if (testChar('0', '9') || testChar('-')) {
			// accepts any glob made up of number characters, gets filtered by the parser ...
			boolean isFloat = false;

			startToken();
			useChar();
			
			while (true) {
				if (testChar('.') || testChar('e') || testChar('E'))
					isFloat = true;
				else if (!testChar('0', '9') && !testChar('-'))
					break;
				
				useChar();
			}

			if (isFloat)
				return finishToken(TokenType.Float);
			else
				return finishToken(TokenType.Integer);
		} else if (testChar('\"')) {
			// "((?:[^\\"]|\\.)*)"
			startToken();
			useChar();

			while (!testChar('\"')) {
				if (isEOF())
					throw new JsonParseException(currentLine, currentColumn, "EOF inside string");
				else if (testControlChar())
					throw new JsonParseException(currentLine, currentColumn, "Invalid control character");
				else if (testChar('\\'))
					useChar();

				useChar();
			}

			useChar();

			return finishToken(TokenType.String);
		} else if (isEOF()) {
			return new Token(TokenType.EndOfFile, "", currentLine, currentColumn); // meaning there are no more tokens
		} else if (testControlChar()) {
			throw new JsonParseException(currentLine, currentColumn, "Invalid control character");
		} else {
			throw new JsonParseException(currentLine, currentColumn, "Invalid token");
		}
	}

	// record line and column number and start token recording
	private void startToken() {
		tokenWriter = new StringWriter();
		tokenLine = currentLine;
		tokenColumn = currentColumn;
	}

	// end token recording and return the read string
	private Token finishToken(TokenType type) {
		String match = tokenWriter.toString();

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
	private Token keywordToken(TokenType type, String keyword) throws IOException {
		int length = keyword.length();

		// we skip testing the frist character as we wouldn't be here it it didn't match 
		useChar();

		for (int i = 1; i < length; i += 1) {
			if (!testChar(keyword.charAt(i)))
				throw new JsonParseException(currentLine, currentColumn - i, "Invalid token"); // i hope we're safe with `currentColumn - i', multi-line keywords aren't that common ;-)

			useChar();
		}

		return new Token(type, keyword, currentLine, currentColumn - length);
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

	// reads one chartacter, return -1 on EOF
	private int readChar() throws IOException {
		return input.read();
	}

	private boolean isEOF() {
		return nextChar == -1;
	}

	public enum TokenType {
		OpenBrace,
		CloseBrace,
		OpenBracket,
		CloseBracket,
		Colon,
		Comma,
		String,
		Integer,
		Float,
		True,
		False,
		Null,
		EndOfFile
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
}
