package relational;

import static relational.old.Join.join;

import java.util.Date;

import relational.old.Join;
import relational.old.Relation;
import relational.old.VariableImpl;

public class DB {
	public static interface TB01_BOOK extends Relation {
		public static final TB01_BOOK T = new TB01_BOOK() {
		};

		public static class V<E> extends VariableImpl<TB01_BOOK, E> {
			private V(String name) {
				super(TB01_BOOK.T, name);
			}
		}

		public static final V<Integer> ID = new V<Integer>("id");
		public static final V<String> TITLE = new V<String>("title");
		public static final V<Integer> AUTHOR_ID = new V<Integer>("author_id");
		public static final V<Integer> PUBLISHER_ID = new V<Integer>(
				"publisher_id");
		public static final V<Date> PUBLICATION = new V<Date>("publication");
		public static final V<Integer> COPIES = new V<Integer>("copies");
		public static final V<Integer> EDITION = new V<Integer>("edition");
	}

	public static interface TB02_AUTHOR extends Relation {
		public static final TB02_AUTHOR T = new TB02_AUTHOR() {
		};

		public static class V<E> extends VariableImpl<TB02_AUTHOR, E> {
			private V(String name) {
				super(TB02_AUTHOR.T, name);
			}
		}

		public static final V<Integer> ID = new V<Integer>("id");
		public static final V<String> NAME = new V<String>("name");
	}

	public static interface TB03_PUBLISHER extends Relation {
		public static final TB03_PUBLISHER T = new TB03_PUBLISHER() {
		};

		public static class V<E> extends VariableImpl<TB03_PUBLISHER, E> {
			private V(String name) {
				super(TB03_PUBLISHER.T, name);
			}
		}

		public static final V<Integer> ID = new V<Integer>("id");
		public static final V<String> NAME = new V<String>("name");
	}

	public static interface TB01_BOOKxTB02_AUTHOR extends TB01_BOOK,
			TB02_AUTHOR {
		public static final TB01_BOOKxTB02_AUTHOR T = new TB01_BOOKxTB02_AUTHOR() {
		};
		public static final Join<TB01_BOOK, TB02_AUTHOR> JOIN = join(
				TB01_BOOK.T, TB02_AUTHOR.T).on(TB01_BOOK.AUTHOR_ID,
				TB02_AUTHOR.ID);
	}

	public static interface TB01_BOOKxTB02_AUTHORxTB03_PUBLISHER extends
			TB01_BOOKxTB02_AUTHOR, TB03_PUBLISHER {
		public static final TB01_BOOKxTB02_AUTHORxTB03_PUBLISHER T = new TB01_BOOKxTB02_AUTHORxTB03_PUBLISHER() {
		};
		public static final Join<TB01_BOOKxTB02_AUTHOR, TB03_PUBLISHER> JOIN = join(
				TB01_BOOKxTB02_AUTHOR.T, TB03_PUBLISHER.T).on(
				TB01_BOOK.PUBLISHER_ID, TB03_PUBLISHER.ID);
	}

	public static interface TB01_BOOKxTB03_PUBLISHER extends TB01_BOOK,
			TB03_PUBLISHER {
		public static final TB01_BOOKxTB03_PUBLISHER T = new TB01_BOOKxTB03_PUBLISHER() {
		};
		public static final Join<TB01_BOOK, TB03_PUBLISHER> JOIN = join(
				TB01_BOOK.T, TB03_PUBLISHER.T).on(TB01_BOOK.PUBLISHER_ID,
				TB03_PUBLISHER.ID);
	}
}