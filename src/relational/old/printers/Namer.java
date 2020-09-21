package relational.old.printers;

import java.io.Serializable;

public interface Namer<E> {
	public static interface Name extends Comparable<Name>, Serializable {
		public String name();

		public String alias();
	}

	public Name name(E e);
}