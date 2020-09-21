package relational.old.expressions;

import relational.old.AggregatedTerm;
import relational.old.Relation;
import relational.old.Term;

public class AggregatedOperation<R extends Relation, E> implements
		AggregatedTerm<R, E> {
	private final Term<R, E> term;
	private final String alias;

	public AggregatedOperation(Term<R, E> term, String alias) {
		super();
		this.term = term;
		this.alias = alias;
	}

	public <V, Exc extends Exception> V accept(Term.Visitor<R, V, Exc> visitor)
			throws Exc {
		return visitor.visit(this);
	}

	public String alias() {
		return this.alias;
	}

	public R relation() {
		return this.term.relation();
	}

	public Term.Precedence precedence() {
		return this.term.precedence();
	}

	public Term<R, E> term() {
		return this.term;
	}
}