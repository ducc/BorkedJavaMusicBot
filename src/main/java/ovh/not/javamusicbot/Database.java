package ovh.not.javamusicbot;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {
    private final StatementManager statementManager;
    public final DataSource dataSource;

    Database(StatementManager statementManager, DataSource dataSource) {
        this.statementManager = statementManager;
        this.dataSource = dataSource;
    }

    public PreparedStatement prepare(Connection connection, Statement statement) throws SQLException {
        return connection.prepareStatement(statementManager.statements.get(statement));
    }
}
