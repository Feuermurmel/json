package ch.feuermurmel.json;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import static ch.feuermurmel.json.Json.convert;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

public final class EqualityTest {
	@Test
	public void testEquality() throws Exception {
		Map<String, Object> filledMap = new HashMap<>();
		
		filledMap.put("a", 1);
		filledMap.put("b", 2);
		
		List<Object> equalityGroups = Arrays.asList(null, false, true, 1, 2, 1., 2., "123", "abc", 'a', Arrays.asList(), Arrays.asList(1, 2, 3), new HashMap<>(), filledMap);
		List<JsonObject> convertedObjects = new ArrayList<>();
		
		for (Object equalityGroup : equalityGroups) {
			convertedObjects.add(convert(equalityGroup));
		}
		
		for (int i = 0; i < equalityGroups.size(); i += 1) {
			Object object = equalityGroups.get(i);
			JsonObject convertedObject = convertedObjects.get(i);
			
			// Object is equal to itself
			Assert.assertThat(convertedObject, is(convertedObject));
			
			// Object is equal to another instance converted from the Java value.
			Assert.assertThat(convertedObject, is(convert(object)));
			
			for (int j = 0; j < equalityGroups.size(); j += 1) {
				// Object is not equal to any of the Java values
				Assert.assertThat(convertedObject, not(equalityGroups.get(j)));
				
				if (i != j)
				// Object is not equal to any of the other converted objects.
				{
					Assert.assertThat(convertedObject, not(convertedObjects.get(j)));
				}
			}
		}
	}
}
