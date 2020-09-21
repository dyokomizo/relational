package orm;

import static relational.old.Term.Parameter.parameter;
import static relational.old.expressions.Operators.TernaryComparison.BETWEEN;

import java.util.ArrayList;
import java.util.List;

import relational.old.Relation;
import relational.old.Term;
import relational.old.Variable;
import relational.old.Term.Parameter;
import relational.old.expressions.Expressions;
import relational.old.expressions.Operators;
import search.criteria.Between;
import search.criteria.Comparison;
import search.criteria.Conjunction;
import search.criteria.Contains;
import search.criteria.Criteria;
import search.criteria.Criterion;
import search.criteria.Like;
import search.criteria.NotContains;
import search.criteria.Criteria.ComparisonOperator;

public class SearchMapper<O, R extends Relation> implements
		Criterion.Visitor<O, Term<R, Boolean>, RuntimeException> {
	public static <O, R extends Relation> SearchMapper<O, R> mapper(
			Mapping<O, R> mapping) {
		return new SearchMapper<O, R>(mapping);
	}

	private final Mapping<O, R> mapping;

	public SearchMapper(Mapping<O, R> mapping) {
		super();
		this.mapping = mapping;
	}

	private <E> Parameter<R, E> $(Class<E> klass) {
		return parameter(mapping.relation(), klass);

	}

	@SuppressWarnings("unchecked")
	public Term<R, Boolean> visit(Contains<O> contains) throws RuntimeException {
		final Variable<R, String> variable = (Variable<R, String>) mapping
				.get(contains.property);
		return Expressions.is(variable, Operators.BinaryStringMatching.LIKE,
				$(String.class));
	}

	@SuppressWarnings("unchecked")
	public Term<R, Boolean> visit(NotContains<O> notContains)
			throws RuntimeException {
		final Variable<R, String> variable = (Variable<R, String>) mapping
				.get(notContains.property);
		return Expressions.is(variable,
				Operators.BinaryStringMatching.NOT_LIKE, $(String.class));
	}

	@SuppressWarnings("unchecked")
	public Term<R, Boolean> visit(Like<O> like) throws RuntimeException {
		final Variable<R, String> variable = (Variable<R, String>) mapping
				.get(like.property);
		return Expressions.is(variable, Operators.BinaryStringMatching.LIKE,
				$(String.class));
	}

	@SuppressWarnings("unchecked")
	public <E extends Comparable<E>> Term<R, Boolean> visit(
			Comparison<O, E> comparison) throws RuntimeException {
		final Operators.BinaryComparison o = convert(comparison.operator);
		final Variable<R, E> variable = (Variable<R, E>) mapping
				.get(comparison.property);
		return Expressions.is(variable, o, $(comparison.property.type()));
	}

	private Operators.BinaryComparison convert(ComparisonOperator operator) {
		switch (operator) {
		case LT:
			return Operators.BinaryComparison.LT;
		case LE:
			return Operators.BinaryComparison.LE;
		case EQ:
			return Operators.BinaryComparison.EQ;
		case NE:
			return Operators.BinaryComparison.NE;
		case GE:
			return Operators.BinaryComparison.GE;
		case GT:
			return Operators.BinaryComparison.GT;
		}
		throw new AssertionError("unreachable");
	}

	@SuppressWarnings("unchecked")
	public <E extends Comparable<E>> Term<R, Boolean> visit(
			Between<O, E> between) throws RuntimeException {
		final Parameter<R, E> $ = $(between.property.type());
		final Variable<R, E> variable = (Variable<R, E>) mapping
				.get(between.property);
		return Expressions.is(variable, BETWEEN, $, $);
	}

	public Term<R, Boolean> visit(Conjunction<O> conjunction)
			throws RuntimeException {
		final List<Term<R, Boolean>> terms = new ArrayList<Term<R, Boolean>>();
		for (final Criterion<O> criterion : conjunction) {
			terms.add(criterion.accept(this));
		}
		return Expressions.and(terms);
	}

	public Term<R, Boolean> visit(Criteria.Constant<O> constant)
			throws RuntimeException {
		return Term.Constant.constant(mapping.relation(), constant.value);
	}
}