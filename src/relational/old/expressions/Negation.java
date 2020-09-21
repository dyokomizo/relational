package relational.old.expressions;

import relational.old.Relation;
import relational.old.Term;

public class Negation<R extends Relation> implements Term<R, Boolean> {
	public static <R extends Relation> Negation<R> not(Term<R, Boolean> term) {
		return new Negation<R>(term);
	}

	private final Term<R, Boolean> t;

	private Negation(Term<R, Boolean> t) {
		super();
		this.t = t;
	}

	public Term<R, Boolean> constraint() {
		return t;
	}

	public <V, Exc extends Exception> V accept(Term.Visitor<R, V, Exc> visitor)
			throws Exc {
		return visitor.visit(this);
	}

	public R relation() {
		return t.relation();
	}

	public Term.Precedence precedence() {
		return Term.Precedence.NEGATION;
	}
}