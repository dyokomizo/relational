package search;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

public class Iterators {
	private static final class Filter<E> implements Iterator<E> {
		private final Iterator<E> iterator;
		private final Predicate<E> criteria;
		private boolean hasCurrent;
		private E current = null;

		private Filter(Predicate<E> criteria, Iterator<E> iterator) {
			super();
			this.criteria = criteria;
			this.iterator = iterator;
			move();
		}

		public boolean hasNext() {
			return this.hasCurrent;
		}

		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			final E element = this.current;
			move();
			return element;
		}

		public void remove() {
			this.iterator.remove();
			move();
		}

		private void move() {
			this.hasCurrent = false;
			while (this.iterator.hasNext()) {
				final E candidate = this.iterator.next();
				if (this.criteria.matches(candidate)) {
					this.current = candidate;
					this.hasCurrent = true;
					return;
				}
			}
		}
	}

	private Iterators() {
		super();
	}

	public static <E> void drop(Iterator<E> iterator, int count) {
		for (int i = 0; (i < count) && iterator.hasNext(); i++) {
			iterator.next();
		}
	}

	public static <E> Iterable<E> iterable(final Iterator<E> iterator) {
		return new Iterable<E>() {
			public Iterator<E> iterator() {
				return iterator;
			}
		};
	}

	public static <E> Iterator<E> filter(final Iterator<E> iterator,
			final Predicate<E> criteria) {
		return new Filter<E>(criteria, iterator);
	}

	public static <E> Set<E> sorted(Collection<E> c, Comparator<E> order) {
		final Set<E> result = new TreeSet<E>(order);
		result.addAll(c);
		return result;
	}
}