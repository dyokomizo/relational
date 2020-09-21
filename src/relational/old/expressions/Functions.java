package relational.old.expressions;

import static relational.old.Term.Constant.constant;
import relational.old.Relation;
import relational.old.Term;
import relational.old.expressions.Expressions.BinaryArithmetic;

@SuppressWarnings("unchecked")
public class Functions {
	private Functions() {
		super();
	}

	public static <R extends Relation, E> Function<R, E> nullIf(Term<R, E> e,
			Term<R, E> expected) {
		return new Function<R, E>("NULLIF", e.relation(), e, expected);
	}

	public static <R extends Relation, E> Function<R, E> coalesce(
			Term<R, E>... ts) {
		return new Function<R, E>("COALESCE", ts[0].relation(), ts);
	}

	public static <R extends Relation, N extends Number> Function<R, N> safeDivision(
			Term<R, N> dividend, Term<R, N> divisor, N zero) {
		final Term<R, N> zeroT = constant(dividend.relation(), zero);
		final Function<R, N> divisorOrNull = nullIf(divisor, zeroT);
		final BinaryArithmetic<R, N> quotientOrNull = Expressions.is(dividend,
				Operators.BinaryArithmetic.DIVIDE, divisorOrNull);
		return coalesce(quotientOrNull, zeroT);
	}
}