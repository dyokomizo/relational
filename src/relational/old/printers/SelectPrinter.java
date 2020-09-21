package relational.old.printers;

import java.lang.reflect.Field;

import relational.old.Classes;
import relational.old.Join;
import relational.old.Relation;
import relational.old.Term;
import relational.old.printers.Namer.Name;

public abstract class SelectPrinter {
	protected final Namer<Relation> namer;
	protected final StringBuilder s;

	public SelectPrinter() {
		super();
		this.namer = new DefaultRelationNamer();
		this.s = new StringBuilder();
	}

	protected <R extends Relation> void where(Term<R, Boolean> constraint) {
		if (constraint == null) {
			return;
		}
		s.append(" where ");
		constraint.accept(new TermPrinter<R>(namer, s));
	}

	protected <R extends Relation, E> void appendTerm(Term<R, E> t) {
		t.accept(new TermPrinter<R>(namer, s));
	}

	protected <R1 extends Relation, R2 extends Relation> void appendJoin(
			Join<R1, R2> join) {
		// TODO add precedence to joins to avoid unnecessary parenthesis
		s.append('(');
		appendRelation(join.r1);
		s.append(" join ");
		appendRelation(join.r2);
		s.append(" on ");
		String separator = "";
		for (final Join.Joint<R1, R2, ?> joint : join) {
			s.append(separator);
			s.append('(');
			appendTerm(joint.v1);
			s.append(" = ");
			appendTerm(joint.v2);
			s.append(')');
		}
		s.append(')');
	}

	protected void appendRelation(Relation r) {
		if (Relation.Do.isJoin(r)) {
			appendJoin(getJoin(r));
		} else {
			final Name name = namer.name(r);
			s.append(name.name()).append(" ").append(name.alias());
		}
	}

	@SuppressWarnings("unchecked")
	protected <R1 extends Relation, R2 extends Relation> Join<R1, R2> getJoin(
			Relation r) {
		try {
			final Field field = Classes.realClass(r).getDeclaredField("JOIN");
			return (Join<R1, R2>) Join.class.cast(field.get(r));
		} catch (RuntimeException exc) {
			throw exc;
		} catch (Exception exc) {
			throw new RuntimeException(exc);
		}
	}
}