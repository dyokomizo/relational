package mock;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jmock.Expectations;

public class SqlExpect {
	public static Expect<PreparedStatement, SQLException> set(final int index,
			final java.sql.Date value) {
		return new Expect<PreparedStatement, SQLException>() {
			public void expect(Expectations e, PreparedStatement ps)
					throws SQLException {
				e.oneOf(ps).setDate(index, value);
			}
		};
	}

	public static Expect<PreparedStatement, SQLException> set(final int index,
			final String value) {
		return new Expect<PreparedStatement, SQLException>() {
			public void expect(Expectations e, PreparedStatement ps)
					throws SQLException {
				e.oneOf(ps).setString(index, value);
			}
		};
	}

	public static Expect<PreparedStatement, SQLException> set(final int index,
			final int value) {
		return new Expect<PreparedStatement, SQLException>() {
			public void expect(Expectations e, PreparedStatement ps)
					throws SQLException {
				e.oneOf(ps).setInt(index, value);
			}
		};
	}

	public static Expect<PreparedStatement, SQLException> set(final int index,
			final long value) {
		return new Expect<PreparedStatement, SQLException>() {
			public void expect(Expectations e, PreparedStatement ps)
					throws SQLException {
				e.oneOf(ps).setObject(index, value, Types.BIGINT);
			}
		};
	}

	public static Expect<PreparedStatement, SQLException> set(final int index,
			final double value) {
		return new Expect<PreparedStatement, SQLException>() {
			public void expect(Expectations e, PreparedStatement ps)
					throws SQLException {
				e.oneOf(ps).setObject(index, value, Types.DOUBLE);
			}
		};
	}

	public static java.sql.Date sql(String text) {
		return new java.sql.Date(java(text).getTime());
	}

	public static java.util.Date java(String text) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(text);
		} catch (ParseException exc) {
			throw new RuntimeException(exc);
		}
	}
}