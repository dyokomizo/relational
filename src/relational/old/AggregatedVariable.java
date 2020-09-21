package relational.old;

public interface AggregatedVariable<R extends Relation, E> extends
		Variable<R, E>, AggregatedTerm<R, E> {
	public Aggregator aggregator();
}