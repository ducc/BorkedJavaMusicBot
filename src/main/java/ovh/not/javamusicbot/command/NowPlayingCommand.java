package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.lib.Song;

import static ovh.not.javamusicbot.Utils.formatDuration;

public class NowPlayingCommand extends Command {
    private static final String NOW_PLAYING_FORMAT = "Currently playing **%s** by **%s** `[%s/%s]`";

    public NowPlayingCommand() {
        super("nowplaying", "current", "now", "np");
    }

    @Override
    public void on(Context context) {
        if (!context.server.isPlaying()) {
            context.reply("No music is playing on this guild!");
            return;
        }
        Song current = context.server.getCurrentSong();
        context.reply(String.format(NOW_PLAYING_FORMAT, current.getTitle(), current.getAuthor(),
                formatDuration(current.getPosition()), formatDuration(current.getDuration())));
    }
}
