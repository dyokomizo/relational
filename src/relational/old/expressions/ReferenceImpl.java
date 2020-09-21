package relational.old.expressions;

import relational.old.Relation;
import relational.old.Term;

public class ReferenceImpl<R extends Relation, E> implements Reference<R, E> {
	private final Term<R, E> term;
	private final String alias;

	public ReferenceImpl(Term<R, E> term, String alias) {
		super();
		this.term = term;
		this.alias = alias;
	}

	public String alias() {
		return this.alias;
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