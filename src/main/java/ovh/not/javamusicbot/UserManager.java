package ovh.not.javamusicbot;

import ovh.not.javamusicbot.impl.DiscordUser;
import ovh.not.javamusicbot.lib.user.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    final Map<String, User> users = new HashMap<>();

    private final Database database;

    UserManager(Database database) {
        this.database = database;
    }

    public User get(String id) throws SQLException {
        if (!users.containsKey(id)) {
            User u = new DiscordUser(database, id);
            users.put(id, u);
            return u;
        }
        return users.get(id);
    }
}
