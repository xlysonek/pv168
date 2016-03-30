package cz.muni.fi.pv168.hotelmanager.backend;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;

public class TestsCommon {
	public DataSource getDataSource() throws SQLException {
		EmbeddedDataSource res = new EmbeddedDataSource();
		res.setDatabaseName("memory:test");
		res.setCreateDatabase("create");
		return res;
	}
	
	public String[] readSQLStatements(URL script) {
		try {
			char buf[] = new char[256];
			StringBuilder res = new StringBuilder();
			InputStreamReader reader = new InputStreamReader(script.openStream());
			while (true) {
				int c = reader.read(buf);
				if (c < 0) {
					break;
				}
				res.append(buf, 0, c);
			}
			return res.toString().split(";");
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to read URL '" + script + "': " + e, e);
		}
	}
	
	public void executeSQLScript(DataSource source, URL script) {
		try (Connection conn = source.getConnection()) {
			for (String statement : readSQLStatements(script)) {
				String trimmed = statement.trim();
				if (!trimmed.isEmpty()) {
					conn.prepareStatement(trimmed).executeUpdate();
				}
			}
		}
		catch (SQLException e) {
			throw new DatabaseException("Failed to execute SQL script: " + e, e);
		}
	}
	
	public void createTables(DataSource source) throws DatabaseException {
		executeSQLScript(source, RentManager.class.getResource("createTables.sql"));
	}
	
	public void dropTables(DataSource source) throws DatabaseException {
		executeSQLScript(source, RentManager.class.getResource("dropTables.sql"));
	}
}
