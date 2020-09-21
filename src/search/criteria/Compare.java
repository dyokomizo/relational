package search.criteria;

public class Compare {
	public static <E extends Comparable<E>> boolean lt(E e1, E e2) {
		return (e1.compareTo(e2) < 0);
	}

	public static <E extends Comparable<E>> boolean le(E e1, E e2) {
		return (e1.compareTo(e2) <= 0);
	}

	public static <E extends Comparable<E>> boolean eq(E e1, E e2) {
		return (e1.compareTo(e2) == 0);
	}

	public static <E extends Comparable<E>> boolean ne(E e1, E e2) {
		return (e1.compareTo(e2) != 0);
	}

	public static <E extends Comparable<E>> boolean ge(E e1, E e2) {
		return (e1.compareTo(e2) >= 0);
	}

	public static <E extends Comparable<E>> boolean gt(E e1, E e2) {
		return (e1.compareTo(e2) > 0);
	}
}