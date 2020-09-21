package search;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;

public interface Property<O, E> extends Ordering<O>, Serializable,
		Comparable<Property<O, E>>, AnnotatedElement {
	public static class Getter<O, E> implements Function<O, E>, Serializable {
		private static final long serialVersionUID = 876142230676368050L;
		private final Property<O, E> property;

		public Getter(Property<O, E> property) {
			super();
			this.property = property;
		}

		public E apply(O obj) {
			return property.get(obj);
		}
	}

	public static class Setter<O, E> implements Function<O, Function<E, Void>>,
			Serializable {
		private static final long serialVersionUID = 876142230676368050L;
		private final Property<O, E> property;

		public Setter(Property<O, E> property) {
			super();
			this.property = property;
		}

		public Function<E, Void> apply(final O obj) {
			return new Function<E, Void>() {
				public Void apply(E e) {
					property.set(obj, e);
					return null;
				}
			};
		}
	}

	public Class<O> declarer();

	public String name();

	public void set(O obj, E value);

	public E get(O obj);

	public Class<E> type();

	public Getter<O, E> getter();

	public Setter<O, E> setter();
}