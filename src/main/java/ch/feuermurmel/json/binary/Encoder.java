package ch.feuermurmel.json.binary;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import ch.feuermurmel.json.*;

import static ch.feuermurmel.json.Json.convert;
import static ch.feuermurmel.json.binary.BinaryJson.*;

final class Encoder {
	private final DataOutputStream output;
	private final Map<JsonObject, Integer> savedObjects = new HashMap<JsonObject, Integer>();
	private int nextSavedObjectId = 0;

	Encoder(OutputStream output) {
		this.output = new DataOutputStream(output);
	}

	public void writeObject(JsonObject obj) throws IOException {
		if (obj instanceof JsonNull) {
			writeTag(tagNull);
		} else if (obj instanceof JsonBoolean) {
			writeBoolean(obj.asBoolean());
		} else if (obj instanceof JsonNumber) {
			if (((JsonNumber) obj).isIntegral())
				writeLong(obj.asLong());
			else
				writeDouble(obj.asDouble());
		} else if (obj instanceof JsonString) {
			writeString(obj.asString());
		} else if (obj instanceof JsonList) {
			writeList(obj.asList());
		} else if (obj instanceof JsonMap) {
			writeMap((JsonMap) obj);
		} else {
			throw new UnsupportedTypeException("Cannot serialize instances of " + obj.getClass().getName());
		}
	}

	private void writeMap(JsonMap obj) throws IOException {
		writeSize(prefixMap, obj.size());

		for (String i : obj) {
			writeSavedObject(convert(i));
			writeObject(obj.get(i));
		}
	}

	private void writeList(JsonList obj) throws IOException {
		writeSize(prefixList, obj.size());

		for (JsonObject i : obj)
			writeObject(i);
	}

	private void writeSavedObject(JsonObject value) throws IOException {
		Integer id = savedObjects.get(value);

		if (id == null) {
			savedObjects.put(value, nextSavedObjectId);
			nextSavedObjectId += 1;

			writeTag(tagSaveAndUseObject);
			writeObject(value);
		} else {
			writeSize(prefixUseSavedObject, id);
		}
	}

	private void writeString(String value) throws IOException {
		byte[] bytes = value.getBytes(charset);

		writeSize(prefixString, bytes.length);
		output.write(bytes);
	}

	private void writeLong(long value) throws IOException {
		if (value < 0)
			writeSize(prefixNegativeInteger, value);
		else
			writeSize(prefixPositiveInteger, value);
	}

	private void writeSize(int prefix, long value) throws IOException {
		long tagValue = value < 0 ? ~value : value;

		// this MAY work ...
		if (tagValue < 24) {
			output.writeByte(prefix + (byte) value + (value < 0 ? 24 : 0));
		} else {
			int length = Long.SIZE - Long.numberOfLeadingZeros(tagValue);

			if (length <= 8) {
				output.writeByte(prefix + size8Bit);
				output.writeByte((byte) value);
			} else if (length <= 16) {
				output.writeByte(prefix + size16Bit);
				output.writeShort((short) value);
			} else if (length <= 32) {
				output.writeByte(prefix + size32Bit);
				output.writeInt((int) value);
			} else if (length <= 64) {
				output.writeByte(prefix + size64Bit);
				output.writeLong(value);
			}
		}
	}

	private void writeDouble(double value) throws IOException {
		writeTag(tagDouble);
		output.writeDouble(value);
	}

	private void writeBoolean(boolean value) throws IOException {
		if (value)
			writeTag(tagTrue);
		else
			writeTag(tagFalse);
	}

	private void writeTag(int tag) throws IOException {
		output.write(tag);
	}
}
