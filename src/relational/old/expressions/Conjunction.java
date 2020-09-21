package relational.old.expressions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import relational.old.Classes;
import relational.old.Relation;
import relational.old.Term;

public class Conjunction<R extends Relation> implements Term<R, Boolean>,
		Iterable<Term<R, Boolean>> {
	public static <R extends Relation> Term<R, Boolean> and(
			List<Term<R, Boolean>> terms) {
		if (terms.isEmpty())
			throw new IllegalArgumentException();
		if (terms.size() == 1)
			return terms.get(0);
		final List<Term<R, Boolean>> list = new ArrayList<Term<R, Boolean>>(
				terms);
		return new Conjunction<R>(Collections.unmodifiableList(list));
	}

	public static <R extends Relation> Term<R, Boolean> and(Term<R, Boolean> t,
			Term<R, Boolean>... ts) {
		if (ts.length == 0)
			return t;
		final List<Term<R, Boolean>> list = Classes.list(t, ts);
		return new Conjunction<R>(Collections.unmodifiableList(list));
	}

	private final List<Term<R, Boolean>> terms;

	private Conjunction(List<Term<R, Boolean>> terms) {
		super();
		this.terms = terms;
	}

	public Iterator<Term<R, Boolean>> iterator() {
		return this.terms.iterator();
	}

	public <V, Exc extends Exception> V accept(Term.Visitor<R, V, Exc> visitor)
			throws Exc {
		return visitor.visit(this);
	}

	public R relation() {
		return this.terms.get(0).relation();
	}

	public Term.Precedence precedence() {
		return Term.Precedence.CONJUNCTION;
	}
}