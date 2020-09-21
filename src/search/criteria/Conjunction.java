package search.criteria;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Conjunction<O> implements Criterion<O>, Iterable<Criterion<O>> {
	private static final long serialVersionUID = -790451765478290538L;
	public final List<Criterion<O>> criteria;

	public Conjunction(Criterion<O>... criteria) {
		this(Collections.unmodifiableList(Arrays.asList(criteria)));
	}

	private Conjunction(List<Criterion<O>> criteria) {
		super();
		this.criteria = criteria;
	}

	public <V, Exc extends Exception> V accept(
			Criterion.Visitor<O, V, Exc> visitor) throws Exc {
		return visitor.visit(this);
	}

	public Iterator<Criterion<O>> iterator() {
		return this.criteria.iterator();
	}
}