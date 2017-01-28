package ovh.not.javamusicbot;

import ovh.not.javamusicbot.impl.DiscordUser;
import ovh.not.javamusicbot.lib.user.User;

import java.util.HashMap;
import java.util.Map;

class UserManager {
    final Map<net.dv8tion.jda.core.entities.User, User> users = new HashMap<>();

    UserManager() {
    }

    User get(net.dv8tion.jda.core.entities.User user) {
        if (!users.containsKey(user)) {
            User u = new DiscordUser(user.getId());
            users.put(user, u);
            return u;
        }
        return users.get(user);
    }
}
