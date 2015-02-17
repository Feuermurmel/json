package ch.feuermurmel.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static ch.feuermurmel.json.Json.convert;
import static ch.feuermurmel.json.Json.list;
import static ch.feuermurmel.json.Json.map;
import static ch.feuermurmel.json.ValidConversionsTest.Coercion.asBoolean;
import static ch.feuermurmel.json.ValidConversionsTest.Coercion.asDouble;
import static ch.feuermurmel.json.ValidConversionsTest.Coercion.asFloat;
import static ch.feuermurmel.json.ValidConversionsTest.Coercion.asInt;
import static ch.feuermurmel.json.ValidConversionsTest.Coercion.asList;
import static ch.feuermurmel.json.ValidConversionsTest.Coercion.asLong;
import static ch.feuermurmel.json.ValidConversionsTest.Coercion.asMap;
import static ch.feuermurmel.json.ValidConversionsTest.Coercion.asString;

@SuppressWarnings("InstanceVariableMayNotBeInitialized")
@RunWith(Parameterized.class)
public final class ValidConversionsTest {
	@Parameterized.Parameter(0)
	public Object javaValue;
	
	@Parameterized.Parameter(1)
	public Coercion validCoercion;
	
	@Test
	public void testCoercion() {
		validCoercion.coerce(convert(javaValue));
	}
	
	public static final List<ValidCoercions> validCoercions = Arrays.asList(
		new ValidCoercions(null),
		new ValidCoercions(true, asBoolean),
		new ValidCoercions(1, asInt, asLong, asFloat, asDouble),
		new ValidCoercions(1.5, asFloat, asDouble),
		new ValidCoercions("schnauz", asString),
		new ValidCoercions(list(), asList),
		new ValidCoercions(map(), asMap)
	);
	
	@Parameterized.Parameters
	public static List<Object[]> parameters() {
		List<Object[]> res = new ArrayList<>();
		
		for (ValidCoercions i : validCoercions) {
			for (Coercion j : i.validCoercions) {
				res.add(new Object[] { i.javaValue, j });
			}
		}
		
		return res;
	}
	
	public static final class ValidCoercions {
		public final Object javaValue;
		public final List<Coercion> validCoercions;
		
		public ValidCoercions(Object javaValue, Coercion... validCoercions) {
			this.javaValue = javaValue;
			this.validCoercions = Arrays.asList(validCoercions);
		}
	}
	
	public enum Coercion {
		asBoolean {
			@Override
			void coerce(JsonObject value) {
				value.asBoolean();
			}
		},
		asInt {
			@Override
			void coerce(JsonObject value) {
				value.asInt();
			}
		},
		asLong {
			@Override
			void coerce(JsonObject value) {
				value.asLong();
			}
		},
		asFloat {
			@Override
			void coerce(JsonObject value) {
				value.asFloat();
			}
		},
		asDouble {
			@Override
			void coerce(JsonObject value) {
				value.asDouble();
			}
		},
		asString {
			@Override
			void coerce(JsonObject value) {
				value.asString();
			}
		},
		asList {
			@Override
			void coerce(JsonObject value) {
				value.asList();
			}
		},
		asMap {
			@Override
			void coerce(JsonObject value) {
				value.asMap();
			}
		};
		
		abstract void coerce(JsonObject value);
	}
}
