package relational.old;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

//TODO add where and having support
public final class GroupBy<R extends Relation> implements Selection<R>,
		Iterable<AggregatedTerm<? super R, ?>> {
	private final R relation;
	private final Set<AggregatedTerm<? super R, ?>> terms;
	private final Set<Variable<? super R, ?>> grouping;
	private final Map<Relation, String> aliases;
	private Term<? super R, Boolean> constraint;

	public static <R extends Relation> GroupBy<R> group(R source) {
		return new GroupBy<R>(source);
	}

	private GroupBy(R relation) {
		super();
		this.relation = relation;
		this.terms = new LinkedHashSet<AggregatedTerm<? super R, ?>>();
		this.grouping = new LinkedHashSet<Variable<? super R, ?>>();
		this.aliases = new HashMap<Relation, String>();
		this.constraint = null;
	}

	public GroupBy<R> by(Variable<? super R, ?>... variables) {
		this.grouping.addAll(Arrays.asList(variables));
		for (final Variable<? super R, ?> v : variables) {
			this.terms.add(Aggregator.groupBy(v));
		}
		return this;
	}

	public GroupBy<R> where(Term<? super R, Boolean> constraint) {
		if (constraint == null)
			throw new IllegalArgumentException("Constraint can't be null");
		this.constraint = constraint;
		return this;
	}

	public GroupBy<R> select(AggregatedTerm<? super R, ?>... term) {
		this.terms.addAll(Arrays.asList(term));
		return this;
	}

	public R relation() {
		return relation;
	}

	public String aliasFor(Relation r) {
		return aliases.get(r);
	}

	public Term<? super R, Boolean> constraint() {
		return constraint;
	}

	public Iterator<AggregatedTerm<? super R, ?>> iterator() {
		return Collections.unmodifiableSet(terms).iterator();
	}

	public Set<Variable<? super R, ?>> grouping() {
		return Collections.unmodifiableSet(grouping);
	}
}