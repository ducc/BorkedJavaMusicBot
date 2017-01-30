package ovh.not.javamusicbot.impl;

import ovh.not.javamusicbot.Database;
import ovh.not.javamusicbot.Statement;
import ovh.not.javamusicbot.lib.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiscordUser implements User {
    private final Database database;
    private final String id;

    public DiscordUser(Database database, String id) throws SQLException {
        this.database = database;
        this.id = id;
        init();
    }

    private void init() throws SQLException {
        try (Connection connection = database.dataSource.getConnection()) {
            PreparedStatement statement = database.prepare(connection, Statement.USER_EXISTS);
            statement.setString(1, getId());
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                statement = database.prepare(connection, Statement.USER_INSERT);
                statement.setString(1, getId());
                statement.execute();
            }
            resultSet.close();
        }
    }

    @Override
    public String getId() {
        return id;
    }
}
