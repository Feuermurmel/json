package ch.feuermurmel.json;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.*;
import javax.sound.sampled.AudioFormat;

public final class JsonString extends JsonObject {
	private final String value;

	public JsonString(String value) {
		this.value = value;
	}
	
	@Override
	public String asString() {
		return value;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("\"");
		for (int i = 0; i < value.length(); i += 1) {
			char c = value.charAt(i);

			if (c ==  '\"')
				builder.append("\\\"");
			else if (c ==  '\\')
				builder.append("\\\\");
			else if (c ==  '\n')
				builder.append("\\n");
			else if (c ==  '\t')
				builder.append("\\t");
			else if (c < (char) 32 || c > (char) 126)
				builder.append(String.format("\\u%04x", (int) c));
			else
				builder.append(c);
		}
		
		builder.append("\"");
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonString.class)
			return false;

		return ((JsonString) obj).value.equals(value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public JsonString clone() {
		return this;
	}
}
