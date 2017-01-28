package ovh.not.javamusicbot.lib.server;

import ovh.not.javamusicbot.lib.user.User;

public interface ServerInfo {
    String getId();

    User getOwner();
}
