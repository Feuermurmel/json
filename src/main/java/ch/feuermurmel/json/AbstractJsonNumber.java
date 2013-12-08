package ch.feuermurmel.json;

abstract class AbstractJsonNumber extends AbstractJsonObject {
	@Override
	public final boolean isNumber() {
		return true;
	}
}
