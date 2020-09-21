package relational.old.printers;

import relational.old.GroupBy;
import relational.old.Relation;
import relational.old.Term;

public class GroupByPrinter extends SelectPrinter {
	public static <R extends Relation> String print(GroupBy<R> query) {
		final GroupByPrinter printer = new GroupByPrinter();
		try {
			printer.select(query);
			printer.from(query);
			printer.where(query.constraint());
			printer.groupBy(query);
			return printer.print();
		} catch (RuntimeException exc) {
			throw new RuntimeException(printer.print(), exc);
		}
	}

	public GroupByPrinter() {
		super();
	}

	public String print() {
		return s.toString();
	}

	private <R extends Relation> void select(GroupBy<R> query) {
		s.append("select ");
		String separator = "";
		for (final Term<? extends Relation, ?> t : query) {
			s.append(separator);
			appendTerm(t);
			separator = ", ";
		}
	}

	private <R extends Relation> void from(GroupBy<R> query) {
		s.append(" from ");
		appendRelation(query.relation());
	}

	private <R extends Relation> void groupBy(GroupBy<R> query) {
		s.append(" group by ");
		String separator = "";
		for (final Term<? extends Relation, ?> v : query.grouping()) {
			s.append(separator);
			appendTerm(v);
			separator = ", ";
		}
	}
}