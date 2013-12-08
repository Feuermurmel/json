package ch.feuermurmel.json;

import java.lang.management.OperatingSystemMXBean;
import java.util.*;

import org.junit.Test;

import static ch.feuermurmel.json.Json.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static ch.feuermurmel.json.JsonObjectTypeTest.Attribute.*;

public final class JsonObjectTypeTest {
	@Test
	public void testTypeAttributes() throws Exception {
		checkType(null, isNull);
		checkType(false, isBoolean);
		checkType(1, isNumber, isIntegral);
		checkType(1., isNumber, isFloating);
		checkType("123", isString);
		checkType(list(), isList);
		checkType(map(), isMap);
	}

	static void checkType(Object value, Attribute... expectedAttributes) {
		Set<Attribute> expectedAttributesSet = new HashSet<>(Arrays.asList(expectedAttributes));
		
		JsonObject instance = convert(value);
		
		for (Attribute i : values()) {
			assertThat(i.getValue(instance), is(expectedAttributesSet.contains(i)));
		}
	}
	
	public enum Attribute {
		isNull {
			@Override
			boolean getValue(JsonObject instance) {
				return instance.isNull();
			}
		}, isBoolean {
			@Override
			boolean getValue(JsonObject instance) {
				return instance.isBoolean();
			}
		}, isNumber {
			@Override
			boolean getValue(JsonObject instance) {
				return instance.isNumber();
			}
		}, isIntegral {
			@Override
			boolean getValue(JsonObject instance) {
				return instance.isIntegral();
			}
		}, isFloating {
			@Override
			boolean getValue(JsonObject instance) {
				return instance.isFloating();
			}
		}, isString {
			@Override
			boolean getValue(JsonObject instance) {
				return instance.isString();
			}
		}, isList {
			@Override
			boolean getValue(JsonObject instance) {
				return instance.isList();
			}
		}, isMap {
			@Override
			boolean getValue(JsonObject instance) {
				return instance.isMap();
			}
		};

		abstract boolean getValue(JsonObject instance);
	}
}
