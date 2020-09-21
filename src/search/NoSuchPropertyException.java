package search;

public class NoSuchPropertyException extends RuntimeException {
	private static final long serialVersionUID = 838015645215658183L;
	private final Class<?> bean;
	private final String name;

	public NoSuchPropertyException(Class<?> bean, String name) {
		super("No property <" + name + "> was found at bean <" + bean.getName()
				+ ">.");
		this.bean = bean;
		this.name = name;
	}

	public Class<?> getBean() {
		return bean;
	}

	public String getName() {
		return name;
	}
}