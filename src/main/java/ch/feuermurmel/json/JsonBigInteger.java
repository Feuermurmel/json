package ch.feuermurmel.json;

import java.math.BigInteger;

final class JsonBigInteger extends AbstractJsonNumber {
	private final BigInteger value;
	
	JsonBigInteger(BigInteger value) {
		this.value = value;
	}
	
	@Override
	public boolean isIntegral() {
		return true;
	}
	
	/**
	 * Return the number casted to a BigInteger. This will result in truncation of double values.
	 */
	@Override
	public BigInteger asBigInteger() {
		return value;
	}
	
	/**
	 * Return the number casted to a long.
	 */
	@Override
	public long asLong() {
		return value.longValue();
	}
	
	/**
	 * Return the number casted to a double.
	 */
	@Override
	public double asDouble() {
		return value.doubleValue();
	}
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JsonBigInteger) {
			return value.equals(((JsonBigInteger) obj).value);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
}
