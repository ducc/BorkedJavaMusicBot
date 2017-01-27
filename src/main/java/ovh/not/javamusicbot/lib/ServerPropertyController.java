package ovh.not.javamusicbot.lib;

import java.util.Collection;

interface ServerPropertyController {
    Collection<ServerProperty> getProperties();

    default ServerProperty getProperty(String property) {
        for (ServerProperty serverProperty : getProperties()) {
            if (serverProperty.name.equals(property)) {
                return serverProperty;
            }
        }
        return null;
    }
}
