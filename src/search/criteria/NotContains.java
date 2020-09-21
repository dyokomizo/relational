package search.criteria;

import search.Property;

public class NotContains<O> implements Criterion<O> {
	private static final long serialVersionUID = -2385182670684065409L;
	public final Property<O, String> property;
	public final String value;

	public NotContains(Property<O, String> property, String value) {
		super();
		if (!String.class.equals(property.type()))
			throw new IllegalArgumentException(
					"Contains is valid only for String properties.");
		this.property = property;
		this.value = value;
	}

	public <V, Exc extends Exception> V accept(
			Criterion.Visitor<O, V, Exc> visitor) throws Exc {
		return visitor.visit(this);
	}
}