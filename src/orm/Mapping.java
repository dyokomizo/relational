package orm;

import relational.old.Relation;
import relational.old.Variable;
import search.Property;

public interface Mapping<O, R extends Relation> {
	public R relation();

	public <E> Mapping<O, R> bind(Property<? super O, E> property,
			Variable<? super R, E> variable);

	public <E> Variable<? super R, E> get(Property<? super O, E> property);
}