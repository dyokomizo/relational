package search;

public class PropertyTypeMismatchException extends RuntimeException {
	private static final long serialVersionUID = -6365913571805942574L;
	private final Class<?> bean;
	private final String name;
	private final Class<?> expected;
	private final Class<?> actual;

	public PropertyTypeMismatchException(Class<?> bean, String name,
			Class<?> expected, Class<?> actual) {
		super("Property <" + name + "> of bean <" + bean.getName()
				+ "> was expected to be of type <" + expected.getName()
				+ "> but it was actually was of type <" + actual.getName()
				+ ">.");
		this.bean = bean;
		this.name = name;
		this.expected = expected;
		this.actual = actual;
	}

	public Class<?> getBean() {
		return bean;
	}

	public String getName() {
		return name;
	}

	public Class<?> getExpected() {
		return expected;
	}

	public Class<?> getActual() {
		return actual;
	}
}