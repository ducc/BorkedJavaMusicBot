package ovh.not.javamusicbot;

import ovh.not.javamusicbot.impl.DiscordUser;
import ovh.not.javamusicbot.lib.user.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    final Map<net.dv8tion.jda.core.entities.User, User> users = new HashMap<>();

    private final Database database;

    UserManager(Database database) {
        this.database = database;
    }

    public User get(net.dv8tion.jda.core.entities.User user) throws SQLException {
        if (!users.containsKey(user)) {
            User u = new DiscordUser(database, user);
            users.put(user, u);
            return u;
        }
        return users.get(user);
    }
}
