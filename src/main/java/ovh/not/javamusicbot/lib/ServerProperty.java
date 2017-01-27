package ovh.not.javamusicbot.lib;

public class ServerProperty {
    final String name;
    private Object value;

    public ServerProperty(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public <T> T value() {
        return (T) value;
    }
}
