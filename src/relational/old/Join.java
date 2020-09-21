package relational.old;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import relational.old.printers.DefaultRelationNamer;
import relational.old.printers.Namer;

public class Join<R1 extends Relation, R2 extends Relation> implements
		Iterable<Join.Joint<R1, R2, ?>> {
	public static <R1 extends Relation, R2 extends Relation> Join<R1, R2> join(
			R1 r1, R2 r2) {
		return new Join<R1, R2>(r1, r2);
	}

	public final R1 r1;
	public final R2 r2;
	private final Set<Join.Joint<R1, R2, ?>> joints;

	private Join(R1 r1, R2 r2) {
		super();
		if ((r1 == null) || (r2 == null))
			throw new IllegalArgumentException("Relations can't be null.");
		this.r1 = r1;
		this.r2 = r2;
		this.joints = new LinkedHashSet<Join.Joint<R1, R2, ?>>();
	}

	public <E> Join<R1, R2> on(Variable<? super R1, E> v1,
			Variable<? super R2, E> v2) {
		if ((v1 == null) || (v2 == null))
			throw new IllegalArgumentException("Variables can't be null.");
		final Join.Joint<R1, R2, E> joint = new Join.Joint<R1, R2, E>(v1, v2);
		joints.add(joint);
		return this;
	}

	public Iterator<Join.Joint<R1, R2, ?>> iterator() {
		return Collections.unmodifiableSet(joints).iterator();
	}

	@Override
	public String toString() {
		final Namer.Name n1 = DefaultRelationNamer.nameFor(r1);
		final Namer.Name n2 = DefaultRelationNamer.nameFor(r2);
		return (n1.name() + "join " + n2.name() + " on " + joints);
	}

	public static class Joint<R1 extends Relation, R2 extends Relation, E> {
		public final Variable<? super R1, E> v1;
		public final Variable<? super R2, E> v2;

		private Joint(Variable<? super R1, E> v1, Variable<? super R2, E> v2) {
			super();
			this.v1 = v1;
			this.v2 = v2;
		}

		@Override
		public String toString() {
			return (v1.name() + " = " + v2.name());
		}
	}
}