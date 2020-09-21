package relational.old;

public interface Variable<R extends Relation, E> extends Term<R, E> {
	public R relation();

	public String name();
}