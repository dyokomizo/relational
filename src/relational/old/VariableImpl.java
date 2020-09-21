package relational.old;

public class VariableImpl<R extends Relation, E> implements Variable<R, E> {
	private final R relation;
	private final String name;

	public VariableImpl(R relation, String name) {
		super();
		if (relation == null)
			throw new IllegalArgumentException("Relation can't be null.");
		this.relation = relation;
		this.name = name;
	}

	public <V, Exc extends Exception> V accept(Term.Visitor<R, V, Exc> visitor)
			throws Exc {
		return visitor.visit(this);
	}

	public Term.Precedence precedence() {
		return Term.Precedence.ATOM;
	}

	public R relation() {
		return this.relation;
	}

	public String name() {
		return this.name;
	}

	@Override
	public String toString() {
		return name();
	}

	@Override
	public int hashCode() {
		return relation().hashCode() * 37 + name().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return (this == obj)
				|| ((obj instanceof Variable<?, ?>) && equals((Variable<?, ?>) obj));
	}

	private boolean equals(Variable<?, ?> that) {
		return this.relation.equals(that.relation())
				&& this.name.equals(that.name());
	}
}