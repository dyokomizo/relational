package search.criteria;

import java.io.Serializable;

import search.Property;

public interface Criterion<O> extends Serializable {
	public static enum Type {
		Contains, NotContains, Like, Comparison, Between, Conjunction, Constant
	}

	public interface Parser<O, C extends Criterion<O>> {
		public <E> Criterion<O> parse(Property<O, E> info, Object[] values);
	}

	public <V, Exc extends Exception> V accept(Visitor<O, V, Exc> visitor)
			throws Exc;

	public static interface Visitor<O, V, Exc extends Exception> {
		public V visit(Contains<O> contains) throws Exc;

		public V visit(NotContains<O> notContains) throws Exc;

		public V visit(Like<O> like) throws Exc;

		public <E extends Comparable<E>> V visit(Comparison<O, E> comparison)
				throws Exc;

		public <E extends Comparable<E>> V visit(Between<O, E> between)
				throws Exc;

		public V visit(Conjunction<O> conjunction) throws Exc;

		public V visit(Criteria.Constant<O> constant) throws Exc;
	}
}