package relational.old.printers;

import relational.old.Query;
import relational.old.Relation;
import relational.old.Term;

public class QueryPrinter extends SelectPrinter {
	public static <R extends Relation> String print(Query<R> query) {
		final QueryPrinter printer = new QueryPrinter();
		printer.select(query);
		printer.from(query);
		printer.where(query.constraint());
		return printer.print();
	}

	public QueryPrinter() {
		super();
	}

	public String print() {
		return s.toString();
	}

	private <R extends Relation> void select(Query<R> query) {
		s.append("select ");
		String separator = "";
		for (final Term<? extends Relation, ?> t : query) {
			s.append(separator);
			appendTerm(t);
			separator = ", ";
		}
	}

	private <R extends Relation> void from(Query<R> query) {
		s.append(" from ");
		appendRelation(query.relation());
	}
}