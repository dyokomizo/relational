package search;

import java.io.Serializable;

public class Functions {
	private Functions() {
		super();
	}

	public static <From, Intermediate, To> Function<From, To> compose(
			final Function<From, Intermediate> f,
			final Function<Intermediate, To> g) {
		return new Function<From, To>() {
			public To apply(From a) {
				final Intermediate i = f.apply(a);
				return g.apply(i);
			}
		};
	}

	public static <E1, E2, E3, E4> Function<E1, E4> compose(
			Function<E1, E2> f1, Function<E2, E3> f2, Function<E3, E4> f3) {
		return compose(f1, compose(f2, f3));
	}

	public static <E1, E2, E3, E4, E5> Function<E1, E5> compose(
			Function<E1, E2> f1, Function<E2, E3> f2, Function<E3, E4> f3,
			Function<E4, E5> f4) {
		return compose(f1, compose(f2, compose(f3, f4)));
	}

	public static <E1, E2, E3, E4, E5, E6> Function<E1, E6> compose(
			Function<E1, E2> f1, Function<E2, E3> f2, Function<E3, E4> f3,
			Function<E4, E5> f4, Function<E5, E6> f5) {
		return compose(f1, compose(f2, compose(f3, compose(f4, f5))));
	}

	private static final Function<Object, Object> ID = new Function<Object, Object>() {
		public Object apply(Object a) {
			return a;
		}
	};

	@SuppressWarnings("unchecked")
	public static <E> Function<E, E> id() {
		return (Function<E, E>) ID;
	}

	public static class Constant<A, B> implements Function<A, B>, Serializable {
		private static final long serialVersionUID = -578727083798127359L;
		private final B value;

		public Constant(B value) {
			super();
			this.value = value;
		}

		public B apply(A a) {
			return this.value;
		}
	}

	public static <A, B> Function<A, B> constant(B b) {
		return new Constant<A, B>(b);
	}

	public static class PairFunction<E, F1, F2> implements
			Function<E, Tuple.Pair<F1, F2>>, Serializable {
		private static final long serialVersionUID = -578727083798127359L;
		private final Function<E, F1> f1;
		private final Function<E, F2> f2;

		public PairFunction(Function<E, F1> f1, Function<E, F2> f2) {
			super();
			this.f1 = f1;
			this.f2 = f2;
		}

		public Tuple.Pair<F1, F2> apply(E e) {
			return Tuple.pair(f1.apply(e), f2.apply(e));
		}
	}

	public static <E, F1, F2> Function<E, Tuple.Pair<F1, F2>> pair(
			Function<E, F1> f1, Function<E, F2> f2) {
		return new PairFunction<E, F1, F2>(f1, f2);
	}
}