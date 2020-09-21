package search.criteria;

import search.Property;

public class Comparison<O, E extends Comparable<E>> implements Criterion<O> {
	private static final long serialVersionUID = 1958994177911334271L;
	public final Property<O, E> property;
	public final Criteria.ComparisonOperator operator;
	public final E value;

	public Comparison(Property<O, E> property,
			Criteria.ComparisonOperator operator, E value) {
		super();
		if (!value.getClass().equals(property.type()))
			throw new IllegalArgumentException(
					"Comparison value's type must be assignable to the property's type.");

		this.property = property;
		this.operator = operator;
		this.value = value;
	}

	public <V, Exc extends Exception> V accept(
			Criterion.Visitor<O, V, Exc> visitor) throws Exc {
		return visitor.visit(this);
	}
}