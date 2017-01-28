package ovh.not.javamusicbot.lib.server;

import org.json.JSONObject;

public class ServerProperty {
    final String name;
    private JSONObject value;

    public ServerProperty(String name, JSONObject value) {
        this.name = name;
        this.value = value;
    }

    public JSONObject value() {
        return value;
    }
}
