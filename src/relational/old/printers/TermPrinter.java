package relational.old.printers;

import java.util.LinkedList;

import relational.old.AggregatedVariable;
import relational.old.Aggregator;
import relational.old.Relation;
import relational.old.Term;
import relational.old.Variable;
import relational.old.Term.Constant;
import relational.old.Term.Parameter;
import relational.old.expressions.AggregatedOperation;
import relational.old.expressions.Conjunction;
import relational.old.expressions.Disjunction;
import relational.old.expressions.Function;
import relational.old.expressions.Negation;
import relational.old.expressions.Reference;
import relational.old.expressions.Expressions.BinaryArithmetic;
import relational.old.expressions.Expressions.BinaryMatching;
import relational.old.expressions.Expressions.BinaryOrderCompare;
import relational.old.expressions.Expressions.TernaryOrderCompare;

public final class TermPrinter<R extends Relation> implements
		Term.Visitor<R, StringBuilder, RuntimeException> {
	private final Namer<Relation> namer;
	private final StringBuilder s;
	private final LinkedList<Term.Precedence> precedences;
	private final LinkedList<Boolean> printTermAlias;

	public TermPrinter(Namer<Relation> namer, StringBuilder s) {
		super();
		this.namer = namer;
		this.s = s;
		this.precedences = new LinkedList<Term.Precedence>();
		this.precedences.addLast(Term.Precedence.OUTER);
		this.printTermAlias = new LinkedList<Boolean>();
		this.printTermAlias.addLast(Boolean.TRUE);
	}

	public <E> StringBuilder visit(Constant<R, E> constant) {
		s.append(constant.value);
		return s;
	}

	public <E> StringBuilder visit(Parameter<R, E> parameter) {
		s.append('?');
		return s;
	}

	public <E> StringBuilder visit(Variable<R, E> v) {
		final R r = v.relation();
		final String alias = namer.name(r).alias();
		final String name = v.name();
		s.append(alias).append(".").append(name);
		return s;
	}

	public <E> StringBuilder visit(AggregatedVariable<R, E> v)
			throws RuntimeException {
		final Aggregator aggregator = v.aggregator();
		final R r = v.relation();
		final String alias = namer.name(r).alias();
		s.append(aggregator.prefix(alias + "." + v.name(),
				isPrintTermAlias() ? v.alias() : null));
		return s;
	}

	public <E> StringBuilder visit(Function<R, E> function)
			throws RuntimeException {
		s.append(function.name());
		s.append('(');
		String separator = "";
		for (final Term<R, ?> argument : function) {
			s.append(separator);
			argument.accept(this);
			separator = ", ";
		}
		s.append(')');
		return s;
	}

	public <E> StringBuilder visit(Reference<R, E> reference)
			throws RuntimeException {
		s.append(reference.alias());
		return s;
	}

	private boolean isPrintTermAlias() {
		return Boolean.TRUE.equals(this.printTermAlias.getLast());
	}

	public <E> StringBuilder visit(AggregatedOperation<R, E> aggregator)
			throws RuntimeException {
		if (aggregator.term().precedence().compareTo(Term.Precedence.ATOM) > 0) {
			s.append('(');
		}
		this.printTermAlias.addLast(Boolean.FALSE);
		aggregator.term().accept(this);
		this.printTermAlias.removeLast();
		if (aggregator.term().precedence().compareTo(Term.Precedence.ATOM) > 0) {
			s.append(')');
		}
		if (isPrintTermAlias())
			s.append(" as ").append(aggregator.alias());
		return s;
	}

	public <E extends Comparable<E>> StringBuilder visit(
			BinaryOrderCompare<R, E> compare) {
		if (compare.precedence().compareTo(precedences.getLast()) > 0) {
			s.append('(');
		}
		precedences.addLast(compare.precedence());
		compare.t1.accept(this);
		s.append(' ').append(compare.operator.representation()).append(' ');
		compare.t2.accept(this);
		precedences.removeLast();
		if (compare.precedence().compareTo(precedences.getLast()) > 0) {
			s.append(')');
		}
		return s;
	}

	public <E extends Comparable<E>> StringBuilder visit(
			TernaryOrderCompare<R, E> compare) throws RuntimeException {
		if (compare.precedence().compareTo(precedences.getLast()) > 0) {
			s.append('(');
		}
		precedences.addLast(compare.precedence());
		compare.t1.accept(this);
		s.append(' ').append(compare.operator.representation1()).append(' ');
		compare.t2.accept(this);
		s.append(' ').append(compare.operator.representation2()).append(' ');
		compare.t3.accept(this);
		precedences.removeLast();
		if (compare.precedence().compareTo(precedences.getLast()) > 0) {
			s.append(')');
		}
		return s;
	}

	public <N extends Number> StringBuilder visit(
			BinaryArithmetic<R, N> arithmetic) throws RuntimeException {
		if (arithmetic.precedence().compareTo(precedences.getLast()) > 0) {
			s.append('(');
		}
		precedences.addLast(arithmetic.precedence());
		arithmetic.t1.accept(this);
		s.append(' ').append(arithmetic.operator.representation()).append(' ');
		arithmetic.t2.accept(this);
		precedences.removeLast();
		if (arithmetic.precedence().compareTo(precedences.getLast()) > 0) {
			s.append(')');
		}
		return s;
	}

	public StringBuilder visit(BinaryMatching<R> match) throws RuntimeException {
		if (match.precedence().compareTo(precedences.getLast()) > 0) {
			s.append('(');
		}
		precedences.addLast(match.precedence());
		match.t1.accept(this);
		s.append(' ').append(match.operator.representation()).append(' ');
		match.t2.accept(this);
		precedences.removeLast();
		if (match.precedence().compareTo(precedences.getLast()) > 0) {
			s.append(')');
		}
		return s;
	}

	public StringBuilder visit(Conjunction<R> conjunction) {
		if (conjunction.precedence().compareTo(precedences.getLast()) > 0) {
			s.append('(');
		}
		precedences.addLast(conjunction.precedence());
		String separator = "";
		for (final Term<R, Boolean> term : conjunction) {
			s.append(separator);
			term.accept(this);
			separator = " and ";
		}
		precedences.removeLast();
		if (conjunction.precedence().compareTo(precedences.getLast()) > 0) {
			s.append(')');
		}
		return s;
	}

	public StringBuilder visit(Disjunction<R> disjunction) {
		if (disjunction.precedence().compareTo(precedences.getLast()) > 0) {
			s.append('(');
		}
		precedences.addLast(disjunction.precedence());
		String separator = "";
		for (final Term<R, Boolean> term : disjunction) {
			s.append(separator);
			term.accept(this);
			separator = " or ";
		}
		precedences.removeLast();
		if (disjunction.precedence().compareTo(precedences.getLast()) > 0) {
			s.append(')');
		}
		return s;
	}

	public StringBuilder visit(Negation<R> negation) {
		if (negation.precedence().compareTo(precedences.getLast()) > 0) {
			s.append('(');
		}
		precedences.addLast(negation.precedence());
		s.append("not ");
		negation.constraint().accept(this);
		precedences.removeLast();
		if (negation.precedence().compareTo(precedences.getLast()) > 0) {
			s.append(')');
		}
		return s;
	}
}