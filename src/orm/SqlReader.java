package orm;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlReader {
	private final ResultSet rs;

	public SqlReader(ResultSet rs) {
		super();
		this.rs = rs;
	}

	@SuppressWarnings("unchecked")
	public <E> E find(String column, E ifMissing) throws SQLException {
		if (rs.findColumn(column) > 0)
			return (E) rs.getObject(column);
		return ifMissing;
	}

	public Date findDate(String column, Date ifMissing) throws SQLException {
		return rs.findColumn(column) > 0 ? rs.getDate(column) : ifMissing;
	}

	public Integer findInt(String column, Integer ifMissing)
			throws SQLException {
		return rs.findColumn(column) > 0 ? rs.getInt(column) : ifMissing;
	}

	public Long findLong(String column, Long ifMissing) throws SQLException {
		return rs.findColumn(column) > 0 ? rs.getLong(column) : ifMissing;
	}

	public Double findDouble(String column, Double ifMissing)
			throws SQLException {
		return rs.findColumn(column) > 0 ? rs.getDouble(column) : ifMissing;
	}

	public String findString(String column, String ifMissing)
			throws SQLException {
		return rs.findColumn(column) > 0 ? rs.getString(column) : ifMissing;
	}
}