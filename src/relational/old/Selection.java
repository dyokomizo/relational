package relational.old;

public interface Selection<R extends Relation> {
	public R relation();

	public Term<? super R, Boolean> constraint();
}
