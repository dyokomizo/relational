package relational.old.expressions;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import relational.old.Relation;
import relational.old.Term;

public class Function<R extends Relation, E> implements Term<R, E>,
		Iterable<Term<R, ?>> {
	private final String name;
	private final R relation;
	private final List<Term<R, ?>> parameters;

	public Function(String name, R relation, Term<R, ?>... parameters) {
		super();
		this.name = name;
		this.relation = relation;
		this.parameters = Collections.unmodifiableList(Arrays
				.asList(parameters));
	}

	public <V, Exc extends Exception> V accept(Term.Visitor<R, V, Exc> visitor)
			throws Exc {
		return visitor.visit(this);
	}

	public Term.Precedence precedence() {
		return Term.Precedence.ATOM;
	}

	public String name() {
		return name;
	}

	public R relation() {
		return relation;
	}

	public Iterator<Term<R, ?>> iterator() {
		return this.parameters.iterator();
	}
}