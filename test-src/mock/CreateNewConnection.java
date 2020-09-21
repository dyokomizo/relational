package mock;

import java.sql.Connection;
import java.sql.SQLException;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class CreateNewConnection extends CreateNew<Connection, SQLException> {
	public CreateNewConnection(Mockery context) {
		super(context, Connection.class);
	}

	protected void expectations(Expectations e, Connection connection)
			throws SQLException {
		e.exactly(1).of(connection).close();
	}
}