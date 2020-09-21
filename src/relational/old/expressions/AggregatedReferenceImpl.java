package relational.old.expressions;

import relational.old.AggregatedTerm;
import relational.old.Relation;
import relational.old.Term;

public class AggregatedReferenceImpl<R extends Relation, E> implements
		AggregatedTerm<R, E>, Reference<R, E> {
	private final AggregatedTerm<R, E> term;

	public AggregatedReferenceImpl(AggregatedTerm<R, E> term) {
		super();
		this.term = term;
	}

	public String alias() {
		return this.term.alias();
	}

	public Term<R, E> term() {
		return term;
	}

	public <V, Exc extends Exception> V accept(Term.Visitor<R, V, Exc> visitor)
			throws Exc {
		return visitor.visit(this);
	}

	public Term.Precedence precedence() {
		return term.precedence();
	}

	public R relation() {
		return term.relation();
	}

	@Override
	public String toString() {
		return alias();
	}
}