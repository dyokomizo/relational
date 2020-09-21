package search.criteria;

import java.util.HashMap;
import java.util.Map;

import search.Property;
import search.criteria.Criterion.Parser;
import search.criteria.Criterion.Type;

public class Criteria {
	public static class Constant<O> implements Criterion<O> {
		private static final long serialVersionUID = -5660777513800590708L;
		public final boolean value;

		private Constant(boolean value) {
			super();
			this.value = value;
		}

		public <V, Exc extends Exception> V accept(Visitor<O, V, Exc> visitor)
				throws Exc {
			return visitor.visit(this);
		}
	}

	public static enum ComparisonOperator {
		LT {
			public <E extends java.lang.Comparable<E>> boolean compare(E e1,
					E e2) {
				return Compare.lt(e1, e2);
			};
		},
		LE {
			public <E extends java.lang.Comparable<E>> boolean compare(E e1,
					E e2) {
				return Compare.le(e1, e2);
			};
		},
		EQ {
			public <E extends java.lang.Comparable<E>> boolean compare(E e1,
					E e2) {
				return Compare.eq(e1, e2);
			};
		},
		NE {
			public <E extends java.lang.Comparable<E>> boolean compare(E e1,
					E e2) {
				return Compare.ne(e1, e2);
			};
		},
		GE {
			public <E extends java.lang.Comparable<E>> boolean compare(E e1,
					E e2) {
				return Compare.ge(e1, e2);
			};
		},
		GT {
			public <E extends java.lang.Comparable<E>> boolean compare(E e1,
					E e2) {
				return Compare.gt(e1, e2);
			};
		};
		public abstract <E extends Comparable<E>> boolean compare(E e1, E e2);
	}

	public static <O> Criterion<O> contains(Property<O, String> property,
			String value) {
		return new Contains<O>(property, value);
	}

	public static <O> Criterion<O> notContains(Property<O, String> property,
			String value) {
		return new NotContains<O>(property, value);
	}

	public static <O> Criterion<O> like(Property<O, String> property,
			String value) {
		return new Like<O>(property, value);
	}

	public static <O, E extends Comparable<E>> Criterion<O> is(
			Property<O, E> property, ComparisonOperator operator, E value) {
		return new Comparison<O, E>(property, operator, value);
	}

	public static <O, E extends Comparable<E>> Criterion<O> between(
			Property<O, E> property, E lower, E upper) {
		return new Between<O, E>(property, lower, upper);
	}

	public static <O> Criterion<O> and(Criterion<O>... criteria) {
		if (criteria.length == 0)
			throw new IllegalArgumentException();
		if (criteria.length == 1)
			return criteria[0];
		return new Conjunction<O>(criteria);
	}

	public static <O> Criterion<O> constant(boolean value) {
		return new Criteria.Constant<O>(value);
	}

	@SuppressWarnings("unchecked")
	private static final Map<Criterion.Type, Criterion.Parser> PARSERS = new HashMap<Criterion.Type, Criterion.Parser>() {
		private static final long serialVersionUID = -8348452240763270500L;
		{
			// Between, Conjunction, Constant
			put(Type.Contains, new Parser() {
				public Criterion parse(Property property, Object[] values) {
					return contains(property, (String) values[1]);
				}
			});
			put(Type.NotContains, new Parser() {
				public Criterion parse(Property property, Object[] values) {
					return notContains(property, (String) values[1]);
				}
			});
			put(Type.Like, new Parser() {
				public Criterion parse(Property property, Object[] values) {
					return like(property, (String) values[1]);
				}
			});
			put(Type.Comparison, new Parser() {
				public Criterion parse(Property property, Object[] values) {
					final Criteria.ComparisonOperator operator = Criteria.ComparisonOperator
							.valueOf((String) values[1]);
					return is(property, operator, (Comparable) values[2]);
				}
			});
			put(Type.Between, new Parser() {
				public Criterion parse(Property property, Object[] values) {
					if ((values == null) || (values.length != 3))
						throw new IllegalArgumentException("Invalid values");
					return between(property, (Comparable) values[1],
							(Comparable) values[2]);
				}
			});
		}
	};

	@SuppressWarnings("unchecked")
	public static <O, C extends Criterion<O>> Criterion.Parser<O, C> parserFor(
			Criterion.Type criteria) {
		return (Criterion.Parser<O, C>) PARSERS.get(criteria);
	}
}