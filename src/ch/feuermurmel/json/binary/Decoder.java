package ch.feuermurmel.json.binary;

import ch.feuermurmel.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Decoder {
	private final DataInputStream input;
	private final List<JsonObject> savedObjects = new ArrayList<JsonObject>();
	

	public Decoder(InputStream input) {
		this.input = new DataInputStream(input);
	}

	public JsonObject readObject() throws IOException {
		int tag = readTag();

		if (tag == BinaryJson.tagNull) {
			return Json.convert(null);
		} else if (tag == BinaryJson.tagFalse) {
			return Json.convert(false);
		} else if (tag == BinaryJson.tagTrue) {
			return Json.convert(true);
		} else if (tag == BinaryJson.tagDouble) {
			return Json.convert(readDouble());
		} else if (tag == BinaryJson.tagSaveAndUseObject) {
			JsonObject obj = readObject();
			
			savedObjects.add(obj);
			
			return obj;
		} else if (tag == BinaryJson.tagIncrementalList) {
			return readIncrementalList();
		} else if (tag == BinaryJson.tagEndIncremental) {
			return null; // special case to signal the end of an incremental object.
		} else {
			int prefix = BinaryJson.prefixMask & tag;
			int size = ~BinaryJson.prefixMask & tag;
			
			if (prefix == BinaryJson.prefixUseSavedObject) {
				return savedObjects.get((int) readSize(size)).clone();
			} else if (prefix == BinaryJson.prefixPositiveInteger) {
				return Json.convert(readLong(size));
			} else if (prefix == BinaryJson.prefixNegativeInteger) {
				return Json.convert(readNegativeLong(size));
			} else if (prefix == BinaryJson.prefixString) {
				byte[] bytes = new byte[(int) readSize(size)];
				
				input.readFully(bytes);
				
				return Json.convert(new String(bytes, BinaryJson.charset));
			} else if (prefix == BinaryJson.prefixList) {
				return readList(size);
			} else if (prefix == BinaryJson.prefixMap) {
				return readMap(size);
			} else {
				throw new BinaryJsonDecodeException("Invalid tag");
			}
		}
	}

	private JsonMap readMap(int size) throws IOException {
		JsonMap map = Json.map();
		int length = (int) readSize(size);

		for (int i = 0; i < length; i += 1) {
			String key = readObject().asString();
			
			map.put(key, readObject());
		}

		return map;
	}

	private JsonList readList(int size) throws IOException {
		JsonList list = Json.list();
		int length = (int) readSize(size);

		for (int i = 0; i < length; i += 1)
			list.add(readObject());
		
		return list;
	}

	private long readLong(int size) throws IOException {
		long value = readSize(size);

		if (value < 0)
			throw new BinaryJsonDecodeException("Integer too large for Java");

		return value;
	}

	private long readNegativeLong(int size) throws IOException {
		long value = readSize(size, true);

		if (value >= 0)
			throw new BinaryJsonDecodeException("Integer too large for Java");

		return value;
	}

	private long readSize(int size) throws IOException {
		return readSize(size, false);
	}

	private long readSize(int size, boolean negative) throws IOException {
		if (size < 24) {
			return size - (negative ? 24 : 0);
		} else {
			long value;
			long mask;
			
			if (size == BinaryJson.size8Bit) {
				value = input.readByte();
				mask = BinaryJson.integerMask8Bit;
			} else if (size == BinaryJson.size16Bit) {
				value = input.readShort();
				mask = BinaryJson.integerMask16Bit;
			} else if (size == BinaryJson.size32Bit) {
				value = input.readInt();
				mask = BinaryJson.integerMask32Bit;
			} else if (size == BinaryJson.size64Bit) {
				value = input.readLong();
				mask = BinaryJson.integerMask64Bit;
			} else {
				throw new BinaryJsonDecodeException("Invalid size");
			}

			if (negative)
				return ~mask | value;
			else
				return mask & value;
		}
	}

	private JsonObject readIncrementalList() throws IOException {
		JsonList list = Json.list();
		
		while (true) {
			JsonObject obj = readObject();
			
			if (obj == null)
				return list;
			
			list.add(obj);
		}
	}

	private double readDouble() throws IOException {
		return input.readDouble();
	}

	private int readTag() throws IOException {
		while (true) {
			int tag = input.readByte() & (1 << 8) - 1;
			
			if (tag == BinaryJson.tagSaveObject) {
				savedObjects.add(readObject());
			} else if (tag != BinaryJson.tagIgnored) {
				return tag;
			}
		}
	}
}
