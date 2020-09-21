package orm;

import java.util.Map;
import java.util.TreeMap;

import relational.old.Relation;
import relational.old.Variable;
import search.Property;

public class MappingImpl<O, R extends Relation> implements Mapping<O, R> {
	public static <O, R extends Relation> Mapping<O, R> mapping(Class<O> type,
			R relation) {
		return new MappingImpl<O, R>(type, relation);
	}

	private final Map<Property<? super O, ?>, Variable<? super R, ?>> mappings;
	private final Class<O> type;
	private final R relation;

	public MappingImpl(Class<O> type, R relation) {
		super();
		this.mappings = new TreeMap<Property<? super O, ?>, Variable<? super R, ?>>();
		this.type = type;
		this.relation = relation;
	}

	public <E> Mapping<O, R> bind(Property<? super O, E> property,
			Variable<? super R, E> variable) {
		this.mappings.put(property, variable);
		return this;
	}

	@SuppressWarnings("unchecked")
	public <E> Variable<? super R, E> get(Property<? super O, E> property) {
		return (Variable<? super R, E>) this.mappings.get(property);
	}

	public Class<O> type() {
		return type;
	}

	public R relation() {
		return this.relation;
	}
}