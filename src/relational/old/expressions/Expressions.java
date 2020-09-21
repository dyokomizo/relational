package relational.old.expressions;

import java.util.List;

import relational.old.AggregatedTerm;
import relational.old.Relation;
import relational.old.Term;
import relational.old.Term.Constant;

public class Expressions {
	private Expressions() {
		super();
	}

	public static <R extends Relation> Term<R, Boolean> and(
			List<Term<R, Boolean>> terms) {
		return Conjunction.and(terms);
	}

	public static <R extends Relation> Term<R, Boolean> and(Term<R, Boolean> t,
			Term<R, Boolean>... ts) {
		return Conjunction.and(t, ts);
	}

	public static <R extends Relation> Term<R, Boolean> or(
			List<Term<R, Boolean>> terms) {
		return Disjunction.or(terms);
	}

	public static <R extends Relation> Term<R, Boolean> or(Term<R, Boolean> t,
			Term<R, Boolean>... ts) {
		return Disjunction.or(t, ts);
	}

	public static <R extends Relation> Term<R, Boolean> not(Term<R, Boolean> t) {
		return Negation.not(t);
	}

	public static <R extends Relation, E extends Comparable<E>> BinaryOrderCompare<R, E> is(
			Term<R, E> t1, Operators.BinaryComparison operator, Term<R, E> t2) {
		return new BinaryOrderCompare<R, E>(t1, operator, t2);
	}

	public static <R extends Relation, E extends Comparable<E>> BinaryOrderCompare<R, E> is(
			E e1, Operators.BinaryComparison operator, Term<R, E> t2) {
		final Constant<R, E> t1 = Term.Constant.<R, E> constant(t2.relation(),
				e1);
		return new BinaryOrderCompare<R, E>(t1, operator, t2);
	}

	public static <R extends Relation, E extends Comparable<E>> BinaryOrderCompare<R, E> is(
			Term<R, E> t1, Operators.BinaryComparison operator, E e2) {
		final Constant<R, E> t2 = Term.Constant.<R, E> constant(t1.relation(),
				e2);
		return new BinaryOrderCompare<R, E>(t1, operator, t2);
	}

	public static <R extends Relation, E extends Comparable<E>> BinaryOrderCompare<R, E> is(
			R relation, E e1, Operators.BinaryComparison operator, E e2) {
		final Constant<R, E> t1 = Term.Constant.<R, E> constant(relation, e1);
		final Constant<R, E> t2 = Term.Constant.<R, E> constant(relation, e2);
		return new BinaryOrderCompare<R, E>(t1, operator, t2);
	}

	// TODO create overloaded versions receiving direct values
	public static <R extends Relation, E extends Comparable<E>> TernaryOrderCompare<R, E> is(
			Term<R, E> t1, Operators.TernaryComparison operator, Term<R, E> t2,
			Term<R, E> t3) {
		return new TernaryOrderCompare<R, E>(t1, operator, t2, t3);
	}

	public static <R extends Relation, N extends Number> BinaryArithmetic<R, N> is(
			Term<R, N> t1, Operators.BinaryArithmetic operator, Term<R, N> t2) {
		return new BinaryArithmetic<R, N>(t1, operator, t2);
	}

	public static <R extends Relation, N extends Number> BinaryArithmetic<R, N> is(
			N n1, Operators.BinaryArithmetic operator, Term<R, N> t2) {
		final Constant<R, N> t1 = Term.Constant.<R, N> constant(t2.relation(),
				n1);
		return new BinaryArithmetic<R, N>(t1, operator, t2);
	}

	public static <R extends Relation, N extends Number> BinaryArithmetic<R, N> is(
			Term<R, N> t1, Operators.BinaryArithmetic operator, N n2) {
		final Constant<R, N> t2 = Term.Constant.<R, N> constant(t1.relation(),
				n2);
		return new BinaryArithmetic<R, N>(t1, operator, t2);
	}

	public static <R extends Relation, N extends Number> BinaryArithmetic<R, N> is(
			R relation, N n1, Operators.BinaryArithmetic operator, N n2) {
		final Constant<R, N> t1 = Term.Constant.<R, N> constant(relation, n1);
		final Constant<R, N> t2 = Term.Constant.<R, N> constant(relation, n2);
		return new BinaryArithmetic<R, N>(t1, operator, t2);
	}

	public static <R extends Relation> BinaryMatching<R> is(Term<R, String> t1,
			Operators.BinaryStringMatching operator, Term<R, String> t2) {
		return new BinaryMatching<R>(t1, operator, t2);
	}

	public static <R extends Relation> BinaryMatching<R> is(Term<R, String> t1,
			Operators.BinaryStringMatching operator, String s2) {
		final Constant<R, String> t2 = Term.Constant
				.constant(t1.relation(), s2);
		return new BinaryMatching<R>(t1, operator, t2);
	}

	public static <R extends Relation> BinaryMatching<R> is(String s1,
			Operators.BinaryStringMatching operator, Term<R, String> t2) {
		final Constant<R, String> t1 = Term.Constant
				.constant(t2.relation(), s1);
		return new BinaryMatching<R>(t1, operator, t2);
	}

	public static <R extends Relation, N extends Number> AggregatedTerm<R, N> is(
			AggregatedTerm<R, N> t1, Operators.BinaryArithmetic operator,
			AggregatedTerm<R, N> t2, String alias) {
		final BinaryArithmetic<R, N> operation = new BinaryArithmetic<R, N>(t1,
				operator, t2);
		return aggregate(operation, alias);
	}

	public static <R extends Relation, E> AggregatedOperation<R, E> aggregate(
			Term<R, E> term, String alias) {
		return new AggregatedOperation<R, E>(term, alias);
	}

	public static <R extends Relation, E> Term<R, E> reference(Term<R, E> term,
			String alias) {
		return new ReferenceImpl<R, E>(term, alias);
	}

	@SuppressWarnings("unchecked")
	public static <R extends Relation, E1, E2> Term<R, E2> cast(
			Class<E2> klass, Term<R, E1> term) {
		return (Term<R, E2>) term;
	}

	public static class BinaryOrderCompare<R extends Relation, E extends Comparable<E>>
			implements Term<R, Boolean> {
		public final Term<R, E> t1;
		public final Operators.BinaryComparison operator;
		public final Term<R, E> t2;

		private BinaryOrderCompare(Term<R, E> t1,
				Operators.BinaryComparison operator, Term<R, E> t2) {
			super();
			this.t1 = t1;
			this.operator = operator;
			this.t2 = t2;
		}

		public <V, Exc extends Exception> V accept(
				Term.Visitor<R, V, Exc> visitor) throws Exc {
			return visitor.visit(this);
		}

		public R relation() {
			return t1.relation();
		}

		public Term.Precedence precedence() {
			return Term.Precedence.COMPARISON;
		}
	}

	public static class TernaryOrderCompare<R extends Relation, E extends Comparable<E>>
			implements Term<R, Boolean> {
		public final Term<R, E> t1;
		public final Operators.TernaryComparison operator;
		public final Term<R, E> t2;
		public final Term<R, E> t3;

		private TernaryOrderCompare(Term<R, E> t1,
				Operators.TernaryComparison operator, Term<R, E> t2,
				Term<R, E> t3) {
			super();
			this.t1 = t1;
			this.operator = operator;
			this.t2 = t2;
			this.t3 = t3;
		}

		public <V, Exc extends Exception> V accept(
				Term.Visitor<R, V, Exc> visitor) throws Exc {
			return visitor.visit(this);
		}

		public R relation() {
			return t1.relation();
		}

		public Term.Precedence precedence() {
			return Term.Precedence.COMPARISON;
		}
	}

	public static class BinaryArithmetic<R extends Relation, N extends Number>
			implements Term<R, N> {
		public final Term<R, N> t1;
		public final Operators.BinaryArithmetic operator;
		public final Term<R, N> t2;

		private BinaryArithmetic(Term<R, N> t1,
				Operators.BinaryArithmetic operator, Term<R, N> t2) {
			super();
			this.t1 = t1;
			this.operator = operator;
			this.t2 = t2;
		}

		public <V, Exc extends Exception> V accept(
				Term.Visitor<R, V, Exc> visitor) throws Exc {
			return visitor.visit(this);
		}

		public R relation() {
			return t1.relation();
		}

		public Term.Precedence precedence() {
			return operator.precedence();
		}
	}

	public static class BinaryMatching<R extends Relation> implements
			Term<R, Boolean> {
		public final Term<R, String> t1;
		public final Operators.BinaryStringMatching operator;
		public final Term<R, String> t2;

		private BinaryMatching(Term<R, String> t1,
				Operators.BinaryStringMatching operator, Term<R, String> t2) {
			super();
			this.t1 = t1;
			this.operator = operator;
			this.t2 = t2;
		}

		public <V, Exc extends Exception> V accept(
				Term.Visitor<R, V, Exc> visitor) throws Exc {
			return visitor.visit(this);
		}

		public R relation() {
			return t1.relation();
		}

		public Term.Precedence precedence() {
			return operator.precedence();
		}
	}
}