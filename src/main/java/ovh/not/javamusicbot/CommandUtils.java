package ovh.not.javamusicbot;

abstract class CommandUtils {
    private static final String DURATION_FORMAT = "%02d:%02d";

    protected static String formatDuration(long duration) {
        long absSeconds = Math.abs(duration / 1000);
        return String.format(DURATION_FORMAT, (absSeconds % 3600) / 60, absSeconds % 60);
    }
}
