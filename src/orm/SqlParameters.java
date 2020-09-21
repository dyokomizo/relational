package orm;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import search.Tuple;

public class SqlParameters {
	private static final Map<Class<?>, Java2Sql> VALUES = new HashMap<Class<?>, Java2Sql>();

	public static enum Java2Sql {
		STRING(Types.VARCHAR, String.class), INT(Types.INTEGER, int.class,
				Integer.class), LONG(Types.BIGINT, long.class, Long.class), DOUBLE(
				Types.DOUBLE, double.class, Double.class), DATE(Types.DATE,
				Date.class) {
			@Override
			public Object convert(Object obj) {
				if (obj instanceof java.util.Date) {
					return new java.sql.Date(((java.util.Date) obj).getTime());
				}
				return obj;
			}
		};
		private final int sql;
		private final Class<?>[] java;

		private Java2Sql(int sql, Class<?>... java) {
			this.java = java;
			this.sql = sql;
			register();
		}

		private void register() {
			for (final Class<?> klass : this.java) {
				VALUES.put(klass, this);
			}
		}

		public Object convert(Object value) {
			return value;
		}

		public static Java2Sql valueOf(Class<?> type) {
			final Java2Sql result = VALUES.get(type);
			if (result != null)
				return result;
			if (type == null)
				throw new NullPointerException("Type is null");
			throw new IllegalArgumentException("No Java2Sql enum for "
					+ type.getName() + ".");
		}
	}

	private final List<Tuple.Pair<Integer, Object>> parameters;

	public SqlParameters() {
		super();
		this.parameters = new ArrayList<Tuple.Pair<Integer, Object>>();
	}

	private Tuple.Pair<Integer, Object> parameter(int sql, Object value) {
		return Tuple.pair(sql, value);
	}

	public void add(Date date) {
		final java.sql.Date converted = new java.sql.Date(date.getTime());
		parameters.add(parameter(Types.DATE, converted));
	}

	public <E> void add(Class<E> type, E value) {
		final Java2Sql converter = Java2Sql.valueOf(type);
		final Object converted = converter.convert(value);
		parameters.add(parameter(converter.sql, converted));
	}

	@SuppressWarnings("unchecked")
	public Tuple.Pair<Integer, Object>[] toArray() {
		final Tuple.Pair<Integer, Object>[] array = new Tuple.Pair[parameters
				.size()];
		return parameters.toArray(array);
	}

	public Object[] values() {
		final List<Object> values = new ArrayList<Object>(parameters.size());
		for (final Tuple.Pair<Integer, Object> parameter : parameters) {
			values.add(parameter.second());
		}
		final Object[] array = new Object[values.size()];
		return values.toArray(array);
	}
}