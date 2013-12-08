package ch.feuermurmel.json;

import static org.junit.Assert.fail;

public final class TestUtil {
	private TestUtil() {
	}

	@SuppressWarnings("ThrowableResultOfMethodCallIgnored")
	public static void expectException(Class<? extends Throwable> expected, TestRunnable test) {
		Throwable exception = catchException(test);
		
		if (exception == null)
			fail(String.format("Expected exception was not thrown: %s", expected));
		else {
			Class<? extends Throwable> exceptionType = exception.getClass();
			
			if (exceptionType != expected)
				fail(String.format("Wrong exception was thrown: %s instead of %s", exceptionType, expected));
		}
	}

	public static Throwable catchException(TestRunnable test) {
		try {
			test.run();
		} catch (Throwable e) {
			return e;
		}

		return null;
	}

	public interface TestRunnable {
		void run() throws Throwable;
	}
}
