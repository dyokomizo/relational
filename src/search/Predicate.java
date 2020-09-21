package search;

public interface Predicate<E> {
	public class AsFunction<E> implements Function<E, Boolean> {
		private final Predicate<E> p;

		public AsFunction(Predicate<E> p) {
			super();
			this.p = p;
		}

		public Boolean apply(E element) {
			return this.p.matches(element);
		}
	}

	public class FromFunction<E> implements Predicate<E> {
		private final Function<E, Boolean> f;

		public FromFunction(Function<E, Boolean> f) {
			super();
			this.f = f;
		}

		public boolean matches(E element) {
			return this.f.apply(element);

		}
	}

	public boolean matches(E element);
}