package relational.old;

import relational.old.expressions.AggregatedOperation;
import relational.old.expressions.Conjunction;
import relational.old.expressions.Disjunction;
import relational.old.expressions.Function;
import relational.old.expressions.Negation;
import relational.old.expressions.Reference;
import relational.old.expressions.Expressions.BinaryArithmetic;
import relational.old.expressions.Expressions.BinaryMatching;
import relational.old.expressions.Expressions.BinaryOrderCompare;
import relational.old.expressions.Expressions.TernaryOrderCompare;

public interface Term<R extends Relation, E> {
	public <V, Exc extends Exception> V accept(Visitor<R, V, Exc> visitor)
			throws Exc;

	public R relation();

	public Precedence precedence();

	public static interface Visitor<R extends Relation, V, Exc extends Exception> {
		public <E> V visit(Constant<R, E> constant) throws Exc;

		public <E> V visit(Parameter<R, E> parameter) throws Exc;

		public <E> V visit(Variable<R, E> variable) throws Exc;

		public <E> V visit(AggregatedOperation<R, E> operation) throws Exc;

		public <E> V visit(AggregatedVariable<R, E> variable) throws Exc;

		public <E> V visit(Reference<R, E> reference) throws Exc;

		public <E> V visit(Function<R, E> function) throws Exc;

		public <E extends Comparable<E>> V visit(
				BinaryOrderCompare<R, E> compare) throws Exc;

		public <E extends Comparable<E>> V visit(
				TernaryOrderCompare<R, E> compare) throws Exc;

		public <N extends Number> V visit(BinaryArithmetic<R, N> arithmetic)
				throws Exc;

		public V visit(BinaryMatching<R> match) throws Exc;

		public V visit(Conjunction<R> conjunction) throws Exc;

		public V visit(Disjunction<R> disjunction) throws Exc;

		public V visit(Negation<R> negation) throws Exc;
	}

	public enum Precedence implements Comparable<Precedence> {
		ATOM, MULTIPLICATIVE, ADDITIVE, COMPARISON, NEGATION, CONJUNCTION, DISJUNCTION, OUTER
	}

	public static class Constant<R extends Relation, E> implements Term<R, E> {
		public static final <R extends Relation, E> Constant<R, E> constant(
				R relation, E value) {
			return new Constant<R, E>(relation, value);
		}

		public final R relation;
		public final E value;

		private Constant(R relation, E value) {
			super();
			this.relation = relation;
			this.value = value;
		}

		public <V, Exc extends Exception> V accept(
				Term.Visitor<R, V, Exc> visitor) throws Exc {
			return visitor.visit(this);
		}

		public R relation() {
			return this.relation;
		}

		public Term.Precedence precedence() {
			return Term.Precedence.ATOM;
		}
	}

	public static class Parameter<R extends Relation, E> implements Term<R, E> {
		public static final <R extends Relation, E> Parameter<R, E> parameter(
				R relation, Class<E> klass) {
			return new Parameter<R, E>(relation);
		}

		public final R relation;

		private Parameter(R relation) {
			super();
			this.relation = relation;
		}

		public <V, Exc extends Exception> V accept(
				Term.Visitor<R, V, Exc> visitor) throws Exc {
			return visitor.visit(this);
		}

		public R relation() {
			return this.relation;
		}

		public Term.Precedence precedence() {
			return Term.Precedence.ATOM;
		}
	}
}