package search;

import java.io.Serializable;

public abstract class Tuple implements Serializable {
	private static final long serialVersionUID = -4312384866361933756L;

	public static class Void extends Tuple {
		private static final long serialVersionUID = -3864349543582457634L;

		private Void() {
			super();
		}
	}

	public static class T<A, TS extends Tuple> extends Tuple {
		private static final long serialVersionUID = -227663141825062101L;
		public final A element;
		public final TS rest;

		private T(A a, TS ts) {
			super();
			this.element = a;
			this.rest = ts;
		}
	}

	public static class Pair<A, B> extends T<A, T<B, Void>> {
		private static final long serialVersionUID = -4686259448123911720L;

		public Pair(A a, B b) {
			super(a, tuple(b, VOID));
		}

		public A first() {
			return element;
		}

		public B second() {
			return rest.element;
		}
	}

	public static class Triple<A, B, C> extends T<A, T<B, T<C, Void>>> {
		private static final long serialVersionUID = -4686259448123911720L;

		public Triple(A a, B b, C c) {
			super(a, tuple(b, tuple(c, VOID)));
		}

		public A first() {
			return element;
		}

		public B second() {
			return rest.element;
		}

		public C third() {
			return rest.rest.element;
		}
	}

	public static final Void VOID = new Void();

	public static final <A, TS extends Tuple> T<A, TS> tuple(A element, TS rest) {
		return new T<A, TS>(element, rest);
	}

	public static final <A> T<A, Void> tuple(A e1) {
		return new T<A, Void>(e1, VOID);
	}

	public static final <A, B> T<A, T<B, Void>> tuple(A e1, B e2) {
		return tuple(e1, tuple(e2));
	}

	public static final <A, B> Pair<A, B> pair(A e1, B e2) {
		return new Pair<A, B>(e1, e2);
	}

	public static final <A, B, C> T<A, T<B, T<C, Void>>> tuple(A e1, B e2, C e3) {
		return tuple(e1, tuple(e2, tuple(e3)));
	}

	public static final <A, B, C> Triple<A, B, C> triple(A e1, B e2, C e3) {
		return new Triple<A, B, C>(e1, e2, e3);
	}

	public static final <A, B, C, D> T<A, T<B, T<C, T<D, Void>>>> tuple(A e1,
			B e2, C e3, D e4) {
		return tuple(e1, tuple(e2, tuple(e3, tuple(e4))));
	}

	public static final <A, B, C, D, E> T<A, T<B, T<C, T<D, T<E, Void>>>>> tuple(
			A e1, B e2, C e3, D e4, E e5) {
		return tuple(e1, tuple(e2, tuple(e3, tuple(e4, tuple(e5)))));
	}

	public static final <E1, TS extends Tuple> E1 e1(T<E1, TS> t) {
		return t.element;
	}

	public static final <E1, E2, TS extends Tuple> E2 e2(T<E1, T<E2, TS>> t) {
		return e1(t.rest);
	}

	public static final <E1, E2, E3, TS extends Tuple> E3 e3(
			T<E1, T<E2, T<E3, TS>>> t) {
		return e2(t.rest);
	}

	public static final <E1, E2, E3, E4, TS extends Tuple> E4 e4(
			T<E1, T<E2, T<E3, T<E4, TS>>>> t) {
		return e3(t.rest);
	}

	public static final <E1, E2, E3, E4, E5, TS extends Tuple> E5 e5(
			T<E1, T<E2, T<E3, T<E4, T<E5, TS>>>>> t) {
		return e4(t.rest);
	}

	private Tuple() {
		super();
	}
}