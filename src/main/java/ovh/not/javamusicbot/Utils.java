package ovh.not.javamusicbot;

public abstract class Utils {
    public static final String HASTEBIN_URL = "https://hastebin.com/documents";
    private static final String DURATION_FORMAT = "%02d:%02d";

    public static String formatDuration(long duration) {
        long absSeconds = Math.abs(duration / 1000);
        return String.format(DURATION_FORMAT, (absSeconds % 3600) / 60, absSeconds % 60);
    }
}
