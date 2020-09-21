package mock;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;


public class CreateNewPreparedStatement extends
		CreateNew<PreparedStatement, SQLException> {
	private final List<Expect<PreparedStatement, SQLException>> expectations;

	public CreateNewPreparedStatement(Mockery context,
			List<Expect<PreparedStatement, SQLException>> expectations) {
		super(context, PreparedStatement.class);
		this.expectations = expectations;
	}

	protected void expectations(Expectations e, PreparedStatement ps)
			throws SQLException {
		this.expectations.isEmpty();
		for (final Expect<PreparedStatement, SQLException> expect : this.expectations) {
			expect.expect(e, ps);
		}
		e.exactly(1).of(ps).executeQuery();
		e.will(new CreateNewResultSet(context));
		e.exactly(1).of(ps).close();
	}
}