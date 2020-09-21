package relational.old;

public interface Relation {
	public static class Do {
		private Do() {
			super();
		}

		public static boolean isJoin(Relation r) {
			int count = 0;
			for (final Class<?> klass : Classes.interfaces(r.getClass())) {
				if (klass.equals(Relation.class))
					count++;
			}
			return (count > 1);
		}

	}
}