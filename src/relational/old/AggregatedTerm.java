package relational.old;

public interface AggregatedTerm<R extends Relation, E> extends Term<R, E> {
	public Term<R, E> term();

	public String alias();
}