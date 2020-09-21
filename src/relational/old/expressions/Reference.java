package relational.old.expressions;

import relational.old.Relation;
import relational.old.Term;

public interface Reference<R extends Relation, E> extends Term<R, E> {
	public String alias();
}