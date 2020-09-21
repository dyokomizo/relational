package search.criteria;

import search.Property;

public class Between<O, E extends Comparable<E>> implements Criterion<O> {
	private static final long serialVersionUID = 3787631537455416468L;
	public final Property<O, E> property;
	public final E lower;
	public final E upper;

	public Between(Property<O, E> property, E lower, E upper) {
		super();
		if (!lower.getClass().equals(property.type())
				|| !upper.getClass().equals(property.type()))
			throw new IllegalArgumentException(
					"Between values' type must be assignable to the property's type.");
		this.property = property;
		this.lower = lower;
		this.upper = upper;
	}

	public <V, Exc extends Exception> V accept(
			Criterion.Visitor<O, V, Exc> visitor) throws Exc {
		return visitor.visit(this);
	}
}