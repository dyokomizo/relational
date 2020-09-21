package search;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Ordering<O> {
	public static enum Type {
		Ascending {
			@Override
			public <O, E extends Comparable<E>> Ordering<O> create(
					Property<O, E> property) {
				return Do.ascending(property);
			}
		},
		Descending {
			@Override
			public <O, E extends Comparable<E>> Ordering<O> create(
					Property<O, E> property) {
				return Do.descending(property);
			}
		},
		Sequence {
			@Override
			public <O, E extends Comparable<E>> Ordering<O> create(
					Property<O, E> property) {
				throw new UnsupportedOperationException();
			}
		};
		public abstract <O, E extends Comparable<E>> Ordering<O> create(
				Property<O, E> property);
	}

	public <V, Exc extends Exception> V accept(Visitor<O, V, Exc> visitor)
			throws Exc;

	public static interface Visitor<O, V, Exc extends Exception> {
		public <E extends Comparable<E>> V visit(Ascending<O, E> ascending)
				throws Exc;

		public <E extends Comparable<E>> V visit(Descending<O, E> descending)
				throws Exc;

		public V visit(Sequence<O> sequence) throws Exc;
	}

	public static class Do {
		public static <O, E extends Comparable<E>> Ordering<O> ascending(
				Property<O, E> property) {
			return new Ascending<O, E>(property);
		}

		public static <O, E extends Comparable<E>> Ordering<O> descending(
				Property<O, E> property) {
			return new Descending<O, E>(property);
		}

		public static <O> Ordering<O> orderBy(Ordering<O>... orderings) {
			if (orderings.length == 1)
				return orderings[0];
			return new Sequence<O>(orderings);
		}
	}

	public static class Ascending<O, E extends Comparable<E>> implements
			Ordering<O> {
		public final Property<O, E> property;

		private Ascending(Property<O, E> property) {
			super();
			this.property = property;
		}

		public <V, Exc extends Exception> V accept(Visitor<O, V, Exc> visitor)
				throws Exc {
			return visitor.visit(this);
		}
	}

	public static class Descending<O, E extends Comparable<E>> implements
			Ordering<O> {
		public final Property<O, E> property;

		private Descending(Property<O, E> property) {
			super();
			this.property = property;
		}

		public <V, Exc extends Exception> V accept(Visitor<O, V, Exc> visitor)
				throws Exc {
			return visitor.visit(this);
		}
	}

	public static class Sequence<O> implements Ordering<O> {
		public final List<Ordering<O>> orderings;

		private Sequence(Ordering<O>... orderings) {
			super();
			this.orderings = Collections.unmodifiableList(Arrays
					.asList(orderings));
		}

		public <V, Exc extends Exception> V accept(Visitor<O, V, Exc> visitor)
				throws Exc {
			return visitor.visit(this);
		}
	}
}