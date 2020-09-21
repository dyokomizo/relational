package relational;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import relational.Test.DB.TB01_BOOK;
import relational.Test.DB.TB01_BOOKxTB02_AUTHOR;
import relational.Test.DB.TB02_AUTHOR;


public class Test {
	public static interface API {
		public static interface Tuple {
		}

		public abstract class Rank {
			private Rank() {
				super();
			}

			public static final class Zero extends Rank {
				private Zero() {
					super();
				}
			}

			public static final class Above<R extends Rank> extends Rank {
				private Above() {
					super();
				}
			}

			public static final Zero Z = new Zero();

			public static final <R extends Rank> Above<R> $(R rank) {
				return new Above<R>();
			}
		}

		public static interface Relation<T extends Tuple, R extends Rank> {
			public interface Visitor<V, Exc extends Exception> {
				public <T extends Tuple> V visit(Table<T> table) throws Exc;

				public <T extends Tuple, R extends Rank> V visit(
						Restriction<T, R> restriction) throws Exc;

				public <T extends Tuple, R extends Rank> V visit(
						Selection<T, R> selection) throws Exc;

				public <T extends Tuple, R extends Rank> V visit(
						Grouping<T, R> grouping) throws Exc;

				public <T extends Tuple, R extends Rank> V visit(Join<T, R> join)
						throws Exc;
			}

			public <V, Exc extends Exception> V accept(Visitor<V, Exc> visitor)
					throws Exc;

			public Relation<T, R> where(Term<T, R, Boolean> restriction);

			public Class<T> tuple();

			public Relation<T, R> select(Variable<? super T, R, ?>... vs);

			public Relation<T, Rank.Above<R>> groupBy(
					Variable<? super T, R, ?>... vs);
		}

		public static class Table<T extends Tuple> implements
				Relation<T, Rank.Zero> {
			public static <T extends Tuple> Relation<T, Rank.Zero> table(
					Class<T> tuple, String name) {
				return new Table<T>(tuple, name);
			}

			public static <T extends Tuple> Relation<T, Rank.Zero> table(
					Class<T> tuple) {
				return new Table<T>(tuple);
			}

			public final Class<T> tuple;
			public final String name;

			public Table(Class<T> tuple, String name) {
				super();
				this.tuple = tuple;
				this.name = name;
			}

			public Table(Class<T> tuple) {
				this(tuple, tuple.getSimpleName());
			}

			public <V, Exc extends Exception> V accept(
					Relation.Visitor<V, Exc> visitor) throws Exc {
				return visitor.visit(this);
			}

			public Relation<T, Rank.Zero> where(
					Term<T, Rank.Zero, Boolean> restriction) {
				return new Restriction<T, Rank.Zero>(this, restriction);
			}

			public Relation<T, Rank.Zero> select(
					Variable<? super T, Rank.Zero, ?>... vs) {
				return new Selection<T, Rank.Zero>(this, vs);
			}

			public Relation<T, Rank.Above<Rank.Zero>> groupBy(
					Variable<? super T, Rank.Zero, ?>... vs) {
				return new Grouping<T, Rank.Zero>(this, vs);
			}

			public Class<T> tuple() {
				return this.tuple;
			}
		}

		public interface Term<T extends Tuple, R extends Rank, E> {
			public Class<T> tuple();
		}

		public class Constant<T extends Tuple, R extends Rank, E> implements
				Term<T, R, E> {
			public final Class<T> tuple;
			public final E value;

			public Constant(Class<T> tuple, E value) {
				super();
				this.tuple = tuple;
				this.value = value;
			}

			public Class<T> tuple() {
				return this.tuple;
			}
		}

		public enum Aggregator {
			SUM, AVG, MAX, MIN, COUNT, FIRST, LAST, GROUP_BY;
			public static <T extends Tuple, R extends Rank, E extends Number> Variable<T, Rank.Above<R>, E> sum(
					Variable<T, R, E> variable) {
				throw new UnsupportedOperationException();
			}

			public static <T extends Tuple, R extends Rank, E> Variable<T, Rank.Above<R>, E> lift(
					Variable<T, R, E> v) {
				return new VariableImpl<T, Rank.Above<R>, E>(v);
			}

			public static <T extends Tuple, R extends Rank> List<? extends Variable<? super T, Rank.Above<R>, ?>> lift(
					List<? extends Variable<? super T, R, ?>> vs) {
				final List<Variable<? super T, Rank.Above<R>, ?>> lifted = new ArrayList<Variable<? super T, Rank.Above<R>, ?>>();
				for (final Variable<? super T, R, ?> v : vs) {
					lifted.add(lift(v));
				}
				return lifted;
			}
		}

		public class Lifted<T extends Tuple, R extends Rank, E> implements
				Term<T, Rank.Above<R>, E> {
			public final Aggregator aggregator;
			public final Term<T, R, E> term;

			public Lifted(Aggregator aggregator, Term<T, R, E> term) {
				super();
				this.aggregator = aggregator;
				this.term = term;
			}

			public Class<T> tuple() {
				return this.term.tuple();
			}
		}

		public class Expressions {
			public static <T extends Tuple, R extends Rank> Term<T, R, Boolean> and(
					Term<T, R, Boolean> t, Term<T, R, Boolean>... ts) {
				// TODO Auto-generated method stub
				throw new UnsupportedOperationException();
			}

			public static <T extends Tuple, R extends Rank, E> Term<T, R, Boolean> is(
					Term<T, R, E> t1, BinaryComparisonOperator operator,
					Term<T, R, E> t2) {
				// TODO Auto-generated method stub
				throw new UnsupportedOperationException();
			}

			public static <T extends Tuple, R extends Rank, E> Term<T, R, Boolean> is(
					E e1, BinaryComparisonOperator operator, Term<T, R, E> t2) {
				final Term<T, R, E> t1 = constant(t2.tuple(), e1);
				return is(t1, operator, t2);
			}

			public static <T extends Tuple, R extends Rank, E> Term<T, R, Boolean> is(
					Term<T, R, E> t1, BinaryComparisonOperator operator, E e2) {
				final Term<T, R, E> t2 = constant(t1.tuple(), e2);
				return is(t1, operator, t2);
			}

			public static <T extends Tuple, R extends Rank, E> Term<T, R, E> constant(
					Class<T> tuple, E value) {
				return new API.Constant<T, R, E>(tuple, value);
			}
		}

		public enum BinaryComparisonOperator {
			LT, LE, EQ, NE, GE, GT;
		}

		public interface Variable<T extends Tuple, R extends Rank, E> extends
				Term<T, R, E> {
			public String name();
		}

		public class VariableImpl<T extends Tuple, R extends Rank, E>
				implements Variable<T, R, E> {
			private final Class<T> tuple;
			private final String name;

			public VariableImpl(Variable<T, ?, E> v) {
				this(v.tuple(), v.name());
			}

			public VariableImpl(Class<T> tuple, String name) {
				super();
				this.tuple = tuple;
				this.name = name;
			}

			public Class<T> tuple() {
				return tuple;
			}

			public String name() {
				return name;
			}
		}

		public static class Restriction<T extends Tuple, R extends Rank>
				implements Relation<T, R> {
			public final Relation<T, R> relation;
			public final Term<T, R, Boolean> constraint;

			public Restriction(Relation<T, R> relation,
					Term<T, R, Boolean> constraint) {
				super();
				this.relation = relation;
				this.constraint = constraint;
			}

			public <V, Exc extends Exception> V accept(
					Relation.Visitor<V, Exc> visitor) throws Exc {
				return visitor.visit(this);
			}

			public Class<T> tuple() {
				return this.relation.tuple();
			}

			public Term<T, R, Boolean> constraint() {
				return constraint;
			}

			public Relation<T, R> select(Variable<? super T, R, ?>... vs) {
				return new Selection<T, R>(this, vs);
			}

			@SuppressWarnings("unchecked")
			public Relation<T, R> where(Term<T, R, Boolean> constraint) {
				final Term<T, R, Boolean> conjunction = API.Expressions.and(
						this.constraint, constraint);
				return new Restriction<T, R>(this.relation, conjunction);
			}

			public Relation<T, Rank.Above<R>> groupBy(
					Variable<? super T, R, ?>... vs) {
				return new Grouping<T, R>(this, vs);
			}
		}

		public static class Selection<T extends Tuple, R extends Rank>
				implements Relation<T, R>, Iterable<Variable<? super T, R, ?>> {
			public final Relation<T, R> relation;
			public final List<Variable<? super T, R, ?>> variables;

			public Selection(Relation<T, R> relation,
					Variable<? super T, R, ?>... variables) {
				this(relation, Arrays.asList(variables));
			}

			private Selection(Relation<T, R> relation,
					List<Variable<? super T, R, ?>> variables) {
				super();
				this.relation = relation;
				this.variables = Collections.unmodifiableList(variables);
			}

			public <V, Exc extends Exception> V accept(
					Relation.Visitor<V, Exc> visitor) throws Exc {
				return visitor.visit(this);
			}

			public Class<T> tuple() {
				return this.relation.tuple();
			}

			public List<Variable<? super T, R, ?>> variables() {
				return variables;
			}

			public Iterator<Variable<? super T, R, ?>> iterator() {
				return variables.iterator();
			}

			public Relation<T, R> where(Term<T, R, Boolean> constraint) {
				return new Restriction<T, R>(this.relation, constraint);
			}

			public Relation<T, R> select(Variable<? super T, R, ?>... vs) {
				if (vs.length == 0)
					return this;
				final List<Variable<? super T, R, ?>> variables = new ArrayList<Variable<? super T, R, ?>>(
						this.variables.size() + vs.length);
				variables.addAll(this.variables);
				variables.addAll(Arrays.asList(vs));
				return new Selection<T, R>(this.relation, variables);
			}

			public Relation<T, Rank.Above<R>> groupBy(
					Variable<? super T, R, ?>... vs) {
				return new Grouping<T, R>(this, vs);
			}
		}

		public static class Grouping<T extends Tuple, R extends Rank>
				implements Relation<T, Rank.Above<R>>,
				Iterable<Variable<? super T, Rank.Above<R>, ?>> {
			public final Relation<T, R> relation;
			public final List<Variable<? super T, Rank.Above<R>, ?>> variables;

			public static <T extends Tuple, R extends Rank> Grouping<T, R> grouping(
					Relation<T, R> relation,
					Variable<? super T, R, ?>... variables) {
				return new Grouping<T, R>(relation, variables);
			}

			public Grouping(Relation<T, R> relation,
					Variable<? super T, R, ?>... variables) {
				this(relation, Arrays.asList(variables));
			}

			private Grouping(Relation<T, R> relation,
					List<Variable<? super T, R, ?>> variables) {
				super();
				this.relation = relation;
				this.variables = Collections.unmodifiableList(API.Aggregator
						.lift(variables));
			}

			public <V, Exc extends Exception> V accept(
					Relation.Visitor<V, Exc> visitor) throws Exc {
				return visitor.visit(this);
			}

			public Class<T> tuple() {
				return this.relation.tuple();
			}

			public List<Variable<? super T, Rank.Above<R>, ?>> variables() {
				return variables;
			}

			public Iterator<Variable<? super T, Rank.Above<R>, ?>> iterator() {
				return variables.iterator();
			}

			public Relation<T, Rank.Above<R>> where(
					Term<T, Rank.Above<R>, Boolean> constraint) {
				return new Restriction<T, Rank.Above<R>>(this, constraint);
			}

			public Relation<T, Rank.Above<R>> select(
					Variable<? super T, Rank.Above<R>, ?>... vs) {
				return new Selection<T, Rank.Above<R>>(this, vs);
			}

			public Relation<T, Rank.Above<Rank.Above<R>>> groupBy(
					Variable<? super T, Rank.Above<R>, ?>... vs) {
				final Relation<T, Rank.Above<R>> t = this;
				final Relation<T, Rank.Above<Rank.Above<R>>> g = grouping(t, vs);
				return g;
			}
		}

		public static class Join<T extends Tuple, R extends Rank> implements
				Relation<T, R> {
			public static <T extends Tuple, R extends Rank> Join<T, R> join(
					Relation<? super T, ?> r1, Relation<? super T, ?> r2,
					Class<T> tuple) {
				return new Join<T, R>(r1, r2, tuple);
			}

			public static <T extends Tuple, R extends Rank> Join<T, R> join(
					Relation<? super T, ?> r1, Relation<? super T, ?> r2,
					Class<T> tuple, R rank) {
				return new Join<T, R>(r1, r2, tuple);
			}

			public final Relation<? super T, ?> r1;
			public final Relation<? super T, ?> r2;
			public final Class<T> tuple;

			public Join(Relation<? super T, ?> r1, Relation<? super T, ?> r2,
					Class<T> tuple) {
				super();
				this.r1 = r1;
				this.r2 = r2;
				this.tuple = tuple;
			}

			public <V, Exc extends Exception> V accept(
					Relation.Visitor<V, Exc> visitor) throws Exc {
				return visitor.visit(this);
			}

			public Class<T> tuple() {
				return this.tuple;
			}

			public Relation<T, R> select(Variable<? super T, R, ?>... vs) {
				return new Selection<T, R>(this, vs);
			}

			public Relation<T, R> where(Term<T, R, Boolean> restriction) {
				return new Restriction<T, R>(this, restriction);
			}

			public Relation<T, Rank.Above<R>> groupBy(
					Variable<? super T, R, ?>... vs) {
				return new Grouping<T, R>(this, vs);
			}
		}
	}

	public static interface DB {
		public static interface TB01_BOOK extends API.Tuple {
			public static final API.Relation<TB01_BOOK, API.Rank.Zero> T = API.Table
					.table(TB01_BOOK.class);

			public static class V<E> extends
					API.VariableImpl<TB01_BOOK, API.Rank.Zero, E> {
				private V(String name) {
					super(TB01_BOOK.class, name);
				}
			}

			public static final V<Integer> ID = new V<Integer>("id");
			public static final V<String> TITLE = new V<String>("title");
			public static final V<Integer> AUTHOR_ID = new V<Integer>(
					"author_id");
			public static final V<Integer> PUBLISHER_ID = new V<Integer>(
					"publisher_id");
			public static final V<Date> PUBLICATION = new V<Date>("publication");
			public static final V<Integer> COPIES = new V<Integer>("copies");
			public static final V<Integer> EDITION = new V<Integer>("edition");
		}

		public static interface TB02_AUTHOR extends API.Tuple {
			public static final API.Relation<TB02_AUTHOR, API.Rank.Zero> T = API.Table
					.table(TB02_AUTHOR.class);

			public static class V<E> extends
					API.VariableImpl<TB02_AUTHOR, API.Rank.Zero, E> {
				private V(String name) {
					super(TB02_AUTHOR.class, name);
				}
			}

			public static final V<Integer> ID = new V<Integer>("id");
			public static final V<String> NAME = new V<String>("name");
		}

		public static interface TB03_PUBLISHER extends API.Tuple {
			public static final API.Relation<TB03_PUBLISHER, API.Rank.Zero> T = API.Table
					.table(TB03_PUBLISHER.class);

			public static class V<E> extends
					API.VariableImpl<TB03_PUBLISHER, API.Rank.Zero, E> {
				private V(String name) {
					super(TB03_PUBLISHER.class, name);
				}
			}

			public static final V<Integer> ID = new V<Integer>("id");
			public static final V<String> NAME = new V<String>("name");
		}

		public static interface TB01_BOOKxTB02_AUTHOR extends TB01_BOOK,
				TB02_AUTHOR {
			public static final API.Relation<TB01_BOOKxTB02_AUTHOR, API.Rank.Zero> T = API.Join
					.join(TB01_BOOK.T, TB02_AUTHOR.T,
							TB01_BOOKxTB02_AUTHOR.class);
			// public static final Join<TB01_BOOK, TB02_AUTHOR> JOIN = join(
			// TB01_BOOK.T, TB02_AUTHOR.T).on(TB01_BOOK.AUTHOR_ID,
			// TB02_AUTHOR.ID);
		}
	}

	@SuppressWarnings("unchecked")
	public void testSelect() throws Exception {
		TB01_BOOK.T.select(TB01_BOOK.ID, TB01_BOOK.TITLE);
		DB.TB01_BOOKxTB02_AUTHOR.T.select(TB01_BOOK.TITLE, TB02_AUTHOR.NAME);
	}

	@SuppressWarnings("unchecked")
	public void testGroupBy() throws Exception {
		TB01_BOOK.T.groupBy(TB01_BOOK.ID).select(
				API.Aggregator.sum(TB01_BOOK.COPIES));
		DB.TB01_BOOKxTB02_AUTHOR.T.select(TB01_BOOK.TITLE, TB02_AUTHOR.NAME);
	}

	@SuppressWarnings("unchecked")
	public void testSubSelection() throws Exception {
		API.Join.join(
				TB01_BOOK.T.where(
						API.Expressions.is(TB01_BOOK.ID,
								API.BinaryComparisonOperator.EQ,
								TB01_BOOK.EDITION)).groupBy(TB01_BOOK.ID)
						.select(API.Aggregator.sum(TB01_BOOK.COPIES)).where(
								API.Expressions.is(API.Aggregator
										.sum(TB01_BOOK.COPIES),
										API.BinaryComparisonOperator.GT, 0)),
				TB02_AUTHOR.T, TB01_BOOKxTB02_AUTHOR.class, API.Rank.Z).select(
				TB01_BOOK.COPIES);
		DB.TB01_BOOKxTB02_AUTHOR.T.select(TB01_BOOK.TITLE, TB02_AUTHOR.NAME);
	}

	@SuppressWarnings("unchecked")
	public void testLift() throws Exception {
		API.Aggregator.lift(Arrays.asList(TB01_BOOK.ID));
	}

}