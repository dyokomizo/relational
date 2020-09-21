package relational.old;

public enum Aggregator {
	SUM, AVG, MAX, MIN, COUNT, FIRST, LAST, GROUP_BY {
		@Override
		public String prefix(String name, String alias) {
			return name;
		}
	};
	public String prefix(String name, String alias) {
		final StringBuilder s = new StringBuilder();
		s.append(name().toLowerCase());
		s.append('(');
		s.append(name);
		s.append(')');
		if (alias != null)
			s.append(" as ").append(alias);
		return s.toString();

	}

	public static <R extends Relation, E extends Number> AggregatedVariable<R, E> sum(
			Variable<R, E> variable) {
		return new AggregatedVariableImpl<R, E>(SUM, variable);
	}

	public static <R extends Relation, E extends Number> AggregatedVariable<R, E> sum(
			Variable<R, E> variable, String alias) {
		return new AggregatedVariableImpl<R, E>(SUM, variable, alias);
	}

	public static <R extends Relation, E extends Number> AggregatedVariable<R, E> avg(
			Variable<R, E> variable) {
		return new AggregatedVariableImpl<R, E>(AVG, variable);
	}

	public static <R extends Relation, E extends Number> AggregatedVariable<R, E> avg(
			Variable<R, E> variable, String alias) {
		return new AggregatedVariableImpl<R, E>(AVG, variable, alias);
	}

	public static <R extends Relation, E> AggregatedVariable<R, E> count(
			Variable<R, E> variable) {
		return new AggregatedVariableImpl<R, E>(COUNT, variable);
	}

	public static <R extends Relation, E> AggregatedVariable<R, E> count(
			Variable<R, E> variable, String alias) {
		return new AggregatedVariableImpl<R, E>(COUNT, variable, alias);
	}

	public static <R extends Relation, E extends Comparable<E>> AggregatedVariable<R, E> max(
			Variable<R, E> variable) {
		return new AggregatedVariableImpl<R, E>(MAX, variable);
	}

	public static <R extends Relation, E extends Comparable<E>> AggregatedVariable<R, E> max(
			Variable<R, E> variable, String alias) {
		return new AggregatedVariableImpl<R, E>(MAX, variable, alias);
	}

	public static <R extends Relation, E extends Comparable<E>> AggregatedVariable<R, E> min(
			Variable<R, E> variable) {
		return new AggregatedVariableImpl<R, E>(MIN, variable);
	}

	public static <R extends Relation, E extends Comparable<E>> AggregatedVariable<R, E> min(
			Variable<R, E> variable, String alias) {
		return new AggregatedVariableImpl<R, E>(MIN, variable, alias);
	}

	public static <R extends Relation, E extends Comparable<E>> AggregatedVariable<R, E> first(
			Variable<R, E> variable) {
		return new AggregatedVariableImpl<R, E>(FIRST, variable);
	}

	public static <R extends Relation, E extends Comparable<E>> AggregatedVariable<R, E> first(
			Variable<R, E> variable, String alias) {
		return new AggregatedVariableImpl<R, E>(FIRST, variable, alias);
	}

	public static <R extends Relation, E extends Comparable<E>> AggregatedVariable<R, E> last(
			Variable<R, E> variable) {
		return new AggregatedVariableImpl<R, E>(LAST, variable);
	}

	public static <R extends Relation, E extends Comparable<E>> AggregatedVariable<R, E> last(
			Variable<R, E> variable, String alias) {
		return new AggregatedVariableImpl<R, E>(LAST, variable, alias);
	}

	public static <R extends Relation, E> AggregatedVariable<R, E> groupBy(
			Variable<R, E> variable) {
		return new AggregatedVariableImpl<R, E>(GROUP_BY, variable);
	}

	public static <R extends Relation, E> AggregatedVariable<R, E> groupBy(
			Variable<R, E> variable, String alias) {
		return new AggregatedVariableImpl<R, E>(GROUP_BY, variable, alias);
	}
}