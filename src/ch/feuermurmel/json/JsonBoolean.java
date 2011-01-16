package ch.feuermurmel.json;

import java.io.IOException;
import java.io.OutputStream;

public final class JsonBoolean extends JsonObject {
	private final boolean value;

	@Override
	public boolean asBoolean() {
		return value;
	}

	public JsonBoolean(boolean value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value ? "true" : "false";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonBoolean.class)
			return false;

		return ((JsonBoolean) obj).value == value;
	}

	@Override
	public int hashCode() {
		return Boolean.valueOf(value).hashCode();
	}

	@Override
	public JsonBoolean clone() {
		return this;
	}
}
