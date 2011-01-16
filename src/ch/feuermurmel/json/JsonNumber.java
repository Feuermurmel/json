package ch.feuermurmel.json;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public final class JsonNumber extends JsonObject {
	private final long valueLong;
	private final double valueDouble;

	public JsonNumber(double value) {
		valueLong = 0;
		valueDouble = value;
	}

	public JsonNumber(long value) {
		valueLong = value;
		valueDouble = 0d;
	}
	
	public boolean isInteger() {
		return valueDouble == 0d;
	}

	@Override
	public long asLong() {
		if (isInteger())
			return valueLong;
		else
			return (long) valueDouble;
	}

	@Override
	public double asDouble() {
		if (isInteger())
			return valueLong;
		else
			return valueDouble;
	} 
	
	@Override
	public String toString() {
		if (isInteger())
			return String.valueOf(valueLong);
		else
			return String.valueOf(valueDouble);
	}

	@SuppressWarnings({ "FloatingPointEquality" })
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != JsonNumber.class)
			return false;
		
		if (valueDouble == 0d)
			return ((JsonNumber) obj).valueLong == valueLong;
		else
			return ((JsonNumber) obj).valueDouble == valueDouble;
	}

	@Override
	public int hashCode() {
		if (isInteger())
			return Long.valueOf(valueLong).hashCode();
		else
			return Double.valueOf(valueDouble).hashCode();
	}

	@Override
	public JsonObject clone() {
		return this;
	}
}
