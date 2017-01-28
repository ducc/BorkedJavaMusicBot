package ovh.not.javamusicbot.impl;

import ovh.not.javamusicbot.lib.user.User;

public class DiscordUser implements User {
    private final String id;

    public DiscordUser(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
