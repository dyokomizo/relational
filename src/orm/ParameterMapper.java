package orm;

import relational.old.Relation;
import search.criteria.Between;
import search.criteria.Comparison;
import search.criteria.Conjunction;
import search.criteria.Contains;
import search.criteria.Criteria;
import search.criteria.Criterion;
import search.criteria.Like;
import search.criteria.NotContains;

public class ParameterMapper<O, R extends Relation> implements
		Criterion.Visitor<O, SqlParameters, RuntimeException> {
	public static <O, R extends Relation> ParameterMapper<O, R> parameters(
			Mapping<O, R> mapping) {
		return new ParameterMapper<O, R>(mapping);
	}

	private final SqlParameters parameters;

	public ParameterMapper(Mapping<O, R> mapping) {
		super();
		this.parameters = new SqlParameters();
	}

	public SqlParameters visit(Contains<O> contains) throws RuntimeException {
		parameters.add(String.class, contains.value + "%");
		return parameters;
	}

	public SqlParameters visit(NotContains<O> notContains)
			throws RuntimeException {
		parameters.add(String.class, notContains.value + "%");
		return parameters;
	}

	public SqlParameters visit(Like<O> like) throws RuntimeException {
		parameters.add(String.class, like.value);
		return parameters;
	}

	public <E extends Comparable<E>> SqlParameters visit(
			Comparison<O, E> comparison) throws RuntimeException {
		parameters.add(comparison.property.type(), comparison.value);
		return parameters;
	}

	public <E extends Comparable<E>> SqlParameters visit(Between<O, E> between)
			throws RuntimeException {
		parameters.add(between.property.type(), between.lower);
		parameters.add(between.property.type(), between.upper);
		return parameters;
	}

	public SqlParameters visit(Conjunction<O> conjunction)
			throws RuntimeException {
		for (final Criterion<O> criterion : conjunction) {
			criterion.accept(this);
		}
		return parameters;
	}

	public SqlParameters visit(Criteria.Constant<O> constant)
			throws RuntimeException {
		return this.parameters;
	}
}