package relational.old;

public class AggregatedVariableImpl<R extends Relation, E> implements
		AggregatedVariable<R, E> {
	private final Aggregator aggregator;
	private final String alias;
	private final R relation;
	private final String name;

	public AggregatedVariableImpl(Aggregator aggregator, Variable<R, E> variable) {
		this(aggregator, variable, variable.name());
	}

	public AggregatedVariableImpl(Aggregator aggregator,
			Variable<R, E> variable, String alias) {
		this(aggregator, variable.relation(), variable.name(), alias);
	}

	public AggregatedVariableImpl(Aggregator aggregator, R relation, String name) {
		this(aggregator, relation, name, name);
	}

	public AggregatedVariableImpl(Aggregator aggregator, R relation,
			String name, String alias) {
		super();
		if (relation == null)
			throw new IllegalArgumentException("Relation can't be null.");
		this.relation = relation;
		this.name = name;
		this.aggregator = aggregator;
		this.alias = alias;
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

	public Aggregator aggregator() {
		return this.aggregator;
	}

	public String alias() {
		return alias;
	}

	public Term<R, E> term() {
		return this;
	}

	@Override
	public String toString() {
		return name();
	}
}