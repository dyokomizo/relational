package mock;

import static org.jmock.Expectations.returnValue;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jmock.Expectations;
import org.jmock.Mockery;


public class CreateNewResultSet extends
		CreateNew<ResultSet, SQLException> {
	public CreateNewResultSet(Mockery context) {
		super(context, ResultSet.class);
	}

	protected void expectations(Expectations e, ResultSet rs)
			throws SQLException {
		e.exactly(1).of(rs).next();
		e.exactly(1).of(rs).close();
		e.will(returnValue(false));
	}
}