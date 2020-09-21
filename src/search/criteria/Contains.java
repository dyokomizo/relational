package search.criteria;

import search.Property;

public class Contains<O> implements Criterion<O> {
	private static final long serialVersionUID = 7792516816186398292L;
	public final Property<O, String> property;
	public final String value;

	public Contains(Property<O, String> property, String value) {
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