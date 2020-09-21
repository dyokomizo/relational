package search;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static search.Ordering.Do.descending;
import static search.Ordering.Do.orderBy;
import static search.Page.page;
import static search.criteria.Criteria.contains;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import search.Ordering.Ascending;
import search.Ordering.Descending;
import search.Ordering.Sequence;
import search.criteria.Between;
import search.criteria.Compare;
import search.criteria.Comparison;
import search.criteria.Conjunction;
import search.criteria.Contains;
import search.criteria.Criteria;
import search.criteria.Criterion;
import search.criteria.Like;
import search.criteria.NotContains;


public class SearchDAOTest {
	public static final class SearchableCollection<E> implements Searchable<E> {
		private static final class CriteriaPredicate<E> implements Predicate<E> {
			private final Criterion<E> criteria;

			private CriteriaPredicate(Criterion<E> criteria) {
				super();
				this.criteria = criteria;
			}

			public boolean matches(final E element) {
				return criteria
						.accept(new Criterion.Visitor<E, Boolean, RuntimeException>() {
							public Boolean visit(Contains<E> contains)
									throws RuntimeException {
								final String text = contains.property
										.get(element);
								return text.contains(contains.value);
							}

							public Boolean visit(NotContains<E> notContains)
									throws RuntimeException {
								final String text = notContains.property
										.get(element);
								return !text.contains(notContains.value);
							}

							public Boolean visit(Like<E> like)
									throws RuntimeException {
								throw new UnsupportedOperationException();
							}

							public <V extends Comparable<V>> Boolean visit(
									Comparison<E, V> comparison)
									throws RuntimeException {
								final V value = comparison.property
										.get(element);
								return comparison.operator.compare(value,
										comparison.value);
							}

							public <V extends Comparable<V>> Boolean visit(
									Between<E, V> between)
									throws RuntimeException {
								final V value = between.property.get(element);
								return Compare.le(between.lower, value)
										&& Compare.ge(between.upper, value);
							}

							public Boolean visit(Conjunction<E> conjunction)
									throws RuntimeException {
								for (final Criterion<E> criteria : conjunction.criteria) {
									if (!criteria.accept(this))
										return false;
								}
								return true;
							}

							public Boolean visit(Criteria.Constant<E> constant)
									throws RuntimeException {
								return constant.value;
							}
						});
			}
		}

		private static final class OrderingComparator<O> implements
				Comparator<O> {
			private final Ordering<O> ordering;

			private OrderingComparator(Ordering<O> ordering) {
				super();
				this.ordering = ordering;
			}

			public int compare(final O o1, final O o2) {
				return ordering
						.accept(new Ordering.Visitor<O, Integer, RuntimeException>() {
							public <E extends Comparable<E>> Integer visit(
									Ascending<O, E> ascending)
									throws RuntimeException {
								final E v1 = ascending.property.get(o1);
								final E v2 = ascending.property.get(o2);
								return v1.compareTo(v2);
							}

							public <E extends Comparable<E>> Integer visit(
									Descending<O, E> descending)
									throws RuntimeException {
								final E v1 = descending.property.get(o1);
								final E v2 = descending.property.get(o2);
								return v2.compareTo(v1);
							}

							public Integer visit(Sequence<O> sequence)
									throws RuntimeException {
								for (final Ordering<O> order : sequence.orderings) {
									final int result = order.accept(this);
									if (result != 0)
										return result;
								}
								return 0;
							}

						});
			}
		}

		private final Collection<E> collection;

		private SearchableCollection(Collection<E> collection) {
			super();
			this.collection = collection;
		}

		public Set<E> search(Criterion<E> criteria, Ordering<E> ordering,
				Page page) {
			final Set<E> result = new LinkedHashSet<E>(page.size);
			final Iterator<E> sorted = Iterators.sorted(this.collection,
					comparator(ordering)).iterator();
			final Iterator<E> filtered = Iterators.filter(sorted,
					predicate(criteria));
			Iterators.drop(filtered, page.before());
			for (final E element : Iterators.iterable(filtered)) {
				result.add(element);
				if (result.size() >= page.size)
					break;
			}
			return result;
		}

		private static <E> Predicate<E> predicate(Criterion<E> criteria) {
			return new CriteriaPredicate<E>(criteria);
		}

		private static <E> Comparator<E> comparator(Ordering<E> ordering) {
			return new OrderingComparator<E>(ordering);
		}
	}

	public static class Book {
		public static final ObjectInfo<Book> BOOK_INFO = ObjectInfo
				.reflect(Book.class);
		public static final Property<Book, String> TITLE = BOOK_INFO.get(
				"title", String.class);
		public static final Property<Book, String> AUTHOR = BOOK_INFO.get(
				"author", String.class);
		public static final Property<Book, Integer> EDITION = BOOK_INFO.get(
				"edition", Integer.class);
		private String title;
		private String author;
		private Integer edition;
		private Date publication;

		public Book() {
			super();
		}

		public Book(String title, String author, int edition, Date publication) {
			super();
			this.title = title;
			this.author = author;
			this.edition = edition;
			this.publication = publication;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public Integer getEdition() {
			return edition;
		}

		public void setEdition(Integer edition) {
			this.edition = edition;
		}

		public Date getPublication() {
			return publication;
		}

		public void setPublication(Date publication) {
			this.publication = publication;
		}
	}

	private Searchable<Book> bookstore;

	@Before
	public void initBooks() throws Exception {
		final Collection<Book> books = new ArrayList<Book>();
		books.add(new Book("The art of Foo", "Foo Guy", 10,
				newDate("30/01/2009")));
		books.add(new Book("The art of Foo, revisited", "Foo Guy", 1,
				newDate("29/01/2009")));
		books.add(new Book("Bar Wars", "Bar Girl", 1, newDate("30/01/2009")));
		this.bookstore = new SearchableCollection<Book>(books);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSearch() throws Exception {
		final Criterion<Book> criteria = contains(Book.TITLE, "Foo");
		final Ordering<Book> ordering = orderBy(Book.AUTHOR,
				descending(Book.TITLE));
		{
			final Set<Book> page1 = bookstore.search(criteria, ordering, page(
					0, 1));
			assertThat(page1.size(), equalTo(1));
			{
				final Book theArtOfFooRevisited = page1.iterator().next();
				assertThat("The art of Foo, revisited",
						equalTo(theArtOfFooRevisited.title));
				assertThat("Foo Guy", equalTo(theArtOfFooRevisited.author));
				assertThat(1, equalTo(theArtOfFooRevisited.edition));
				assertThat(newDate("29/01/2009"),
						equalTo(theArtOfFooRevisited.publication));
			}
		}
		{
			final Set<Book> page2 = bookstore.search(criteria, ordering, page(
					1, 1));
			assertThat(page2.size(), equalTo(1));
			{
				final Book theArtOfFoo = page2.iterator().next();
				assertThat("The art of Foo", equalTo(theArtOfFoo.title));
				assertThat("Foo Guy", equalTo(theArtOfFoo.author));
				assertThat(10, equalTo(theArtOfFoo.edition));
				assertThat(newDate("30/01/2009"),
						equalTo(theArtOfFoo.publication));
			}
		}
		{
			final Set<Book> page3 = bookstore.search(criteria, ordering, page(
					2, 1));
			assertThat(page3.size(), equalTo(0));
		}

	}

	@Test
	public void testCreateSearchFromParameters() throws Exception {
		final Map<String, Object[]> criteria = new HashMap<String, Object[]>();
		criteria.put("title", new Object[] { "Contains", "Foo" });
		criteria.put("edition", new Object[] { "Between", 1, 5 });
		final String[] ordering = new String[] { "Descending", "title" };
		final SearchParser<Book> parser = new SearchParser<Book>(Book.BOOK_INFO);
		final Set<Book> page = bookstore.search(parser.parseCriteria(criteria),
				parser.parseOrdering(ordering), page(0, 10));
		assertThat(page.size(), equalTo(1));

	}

	@Test(expected = NoSuchPropertyException.class)
	public void testCreateSearchWithInvalidProperty() throws Exception {
		final Map<String, Object[]> criteria = new HashMap<String, Object[]>();
		criteria.put("rating", new Object[] {});
		final SearchParser<Book> parser = new SearchParser<Book>(Book.BOOK_INFO);
		parser.parseCriteria(criteria);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateSearchWithInsufficientParametersForCriteria()
			throws Exception {
		final Map<String, Object[]> criteria = new HashMap<String, Object[]>();
		criteria.put("edition", new Object[] { "Between" });
		final SearchParser<Book> parser = new SearchParser<Book>(Book.BOOK_INFO);
		parser.parseCriteria(criteria);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateSearchWithIncorrectCriteria() throws Exception {
		final Map<String, Object[]> criteria = new HashMap<String, Object[]>();
		criteria.put("edition", new Object[] { "Contains", "1" });
		final SearchParser<Book> parser = new SearchParser<Book>(Book.BOOK_INFO);
		parser.parseCriteria(criteria);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateSearchWithInvalidParametersForCriteria()
			throws Exception {
		final Map<String, Object[]> criteria = new HashMap<String, Object[]>();
		criteria.put("edition", new Object[] { "Between", "1", 2 });
		final SearchParser<Book> parser = new SearchParser<Book>(Book.BOOK_INFO);
		parser.parseCriteria(criteria);
	}

	public static Date newDate(String text) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(text);
		} catch (ParseException exc) {
			throw new RuntimeException(exc);
		}
	}
}