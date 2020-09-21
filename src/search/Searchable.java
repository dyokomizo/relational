package search;

import java.util.Set;

import search.criteria.Criterion;


public interface Searchable<E> {
	public Set<E> search(Criterion<E> criteria, Ordering<E> ordering, Page page);
}