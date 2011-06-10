package ch.feuermurmel.json.binary;

import ch.feuermurmel.json.*;
import java.io.*;
import java.nio.charset.Charset;

public class BinaryJson {
	static final int tagIgnored = 0;
	static final int tagNull = 1;
	static final int tagFalse = 2;
	static final int tagTrue = 3;
	static final int tagDouble = 4;

	static final int tagSaveObject = 8;
	static final int tagSaveAndUseObject = 9;
	static final int tagIncrementalList = 9;
//	static final int tagIncrementalByteStream = 9;
	static final int tagEndIncremental = 9;

	static final int prefixMask = 7 << 5;
	static final int size8Bit = 24;
	static final int size16Bit = 24 + 1;
	static final int size32Bit = 24 + 2;
	static final int size64Bit = 24 + 3;

	static final long integerMask8Bit = (1l << 8) - 1;
	static final long integerMask16Bit = (1l << 16) - 1;
	static final long integerMask32Bit = (1l << 32) - 1;
	static final long integerMask64Bit = -1l;
	
	static final int prefixUseSavedObject = 1 << 5;
	static final int prefixPositiveInteger = 2 << 5;
	static final int prefixNegativeInteger = 3 << 5;
	static final int prefixString = 4 << 5;
	static final int prefixList = 5 << 5;
	static final int prefixMap = 6 << 5;
//	static final int prefixByteStream = 7 << 5;
	
	static final Charset charset = Charset.forName("UTF-8");

	private BinaryJson() {
	}

	public static void encode(OutputStream stream, JsonObject obj) throws IOException {
		new Encoder(stream).writeObject(obj);
	}

	public static JsonObject decode(InputStream stream) throws IOException {
		return new Decoder(stream).readObject();
	}
}
