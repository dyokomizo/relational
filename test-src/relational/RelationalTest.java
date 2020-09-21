package relational;

import static relational.old.Aggregator.max;
import static relational.old.Aggregator.sum;
import static relational.old.GroupBy.group;
import static relational.old.Query.from;
import static relational.old.expressions.Conjunction.and;
import static relational.old.expressions.Disjunction.or;
import static relational.old.expressions.Expressions.is;
import static relational.old.expressions.Operators.BinaryComparison.EQ;
import static relational.old.expressions.Operators.BinaryComparison.GE;
import static relational.old.expressions.Operators.BinaryComparison.GT;
import static relational.old.printers.GroupByPrinter.print;
import static relational.old.printers.QueryPrinter.print;
import junit.framework.TestCase;
import relational.DB.TB01_BOOK;
import relational.DB.TB01_BOOKxTB02_AUTHOR;
import relational.DB.TB01_BOOKxTB02_AUTHORxTB03_PUBLISHER;
import relational.DB.TB01_BOOKxTB03_PUBLISHER;
import relational.DB.TB02_AUTHOR;
import relational.DB.TB03_PUBLISHER;

@SuppressWarnings("unchecked")
public class RelationalTest extends TestCase {
	public RelationalTest() {
		super();
	}

	public void testSimpleSelect() throws Exception {
		assertEquals("select tb01.title from TB01_BOOK tb01", print(from(
				TB01_BOOK.T).select(TB01_BOOK.TITLE)));
	}

	public void testSelectFromJoin() throws Exception {
		assertEquals(
				"select tb01.title, tb02.name from (TB01_BOOK tb01 join TB02_AUTHOR tb02 on (tb01.author_id = tb02.id))",
				print(from(TB01_BOOKxTB02_AUTHOR.T).select(TB01_BOOK.TITLE,
						TB02_AUTHOR.NAME)));
		assertEquals(
				"select tb01.title, tb03.name from (TB01_BOOK tb01 join TB03_PUBLISHER tb03 on (tb01.publisher_id = tb03.id))",
				print(from(TB01_BOOKxTB03_PUBLISHER.T).select(TB01_BOOK.TITLE,
						TB03_PUBLISHER.NAME)));
	}

	public void testSelectFromMultiRelationJoin() throws Exception {
		assertEquals(
				"select tb01.title, tb02.name, tb03.name from ((TB01_BOOK tb01 join TB02_AUTHOR tb02 on (tb01.author_id = tb02.id)) join TB03_PUBLISHER tb03 on (tb01.publisher_id = tb03.id))",
				print(from(TB01_BOOKxTB02_AUTHORxTB03_PUBLISHER.T).select(
						TB01_BOOK.TITLE, TB02_AUTHOR.NAME, TB03_PUBLISHER.NAME)));
	}

	public void testSelectGroupingBy() throws Exception {
		assertEquals(
				"select tb01.title, sum(tb01.copies) as copies, max(tb01.publication) as publication from TB01_BOOK tb01 group by tb01.title",
				print(group(TB01_BOOK.T).by(TB01_BOOK.TITLE).select(
						sum(TB01_BOOK.COPIES), max(TB01_BOOK.PUBLICATION))));
		assertEquals(
				"select tb02.id, tb02.name, tb01.title, sum(tb01.copies) as copies, max(tb01.publication) as publication from (TB01_BOOK tb01 join TB02_AUTHOR tb02 on (tb01.author_id = tb02.id)) group by tb02.id, tb02.name, tb01.title",
				print(group(TB01_BOOKxTB02_AUTHOR.T).by(TB02_AUTHOR.ID,
						TB02_AUTHOR.NAME, TB01_BOOK.TITLE).select(
						sum(TB01_BOOK.COPIES), max(TB01_BOOK.PUBLICATION))));
	}

	public void testSimpleSelectWhere() throws Exception {
		assertEquals(
				"select tb01.title from TB01_BOOK tb01 where tb01.author_id = 1",
				print(from(TB01_BOOK.T).select(TB01_BOOK.TITLE).where(
						is(TB01_BOOK.AUTHOR_ID, EQ, 1))));
		assertEquals(
				"select tb01.title from TB01_BOOK tb01 where tb01.author_id > tb01.copies",
				print(from(TB01_BOOK.T).select(TB01_BOOK.TITLE).where(
						is(TB01_BOOK.AUTHOR_ID, GT, TB01_BOOK.COPIES))));
	}

	public void testSelectWhereWithPrecedence() throws Exception {
		assertEquals(
				"select tb01.title from TB01_BOOK tb01 where tb01.author_id = 1 and (tb01.copies >= 1000 or tb01.edition > 1)",
				print(from(TB01_BOOK.T).select(TB01_BOOK.TITLE).where(
						and(is(TB01_BOOK.AUTHOR_ID, EQ, 1), or(is(
								TB01_BOOK.COPIES, GE, 1000), is(
								TB01_BOOK.EDITION, GT, 1))))));
	}
}
