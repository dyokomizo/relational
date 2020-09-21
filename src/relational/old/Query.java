package relational.old;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public final class Query<R extends Relation> implements Selection<R>,
		Iterable<Variable<? super R, ?>> {

	public static <R extends Relation> Query<R> from(R source) {
		return new Query<R>(source);
	}

	private final R relation;
	private final Set<Variable<? super R, ?>> variables;
	private Term<? super R, Boolean> constraint;

	private Query(R relation) {
		super();
		this.relation = relation;
		this.variables = new LinkedHashSet<Variable<? super R, ?>>();
		this.constraint = null;
	}

	public Query<R> where(Term<? super R, Boolean> constraint) {
		if (constraint == null)
			throw new IllegalArgumentException("Constraint can't be null");
		this.constraint = constraint;
		return this;
	}

	public Query<R> select(Variable<? super R, ?>... variables) {
		this.variables.addAll(Arrays.asList(variables));
		return this;
	}

	public R relation() {
		return relation;
	}

	public Term<? super R, Boolean> constraint() {
		return constraint;
	}

	public Iterator<Variable<? super R, ?>> iterator() {
		return Collections.unmodifiableSet(variables).iterator();
	}
}