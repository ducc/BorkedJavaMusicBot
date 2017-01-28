package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.Middlewares;
import ovh.not.javamusicbot.lib.song.Song;

import static ovh.not.javamusicbot.Utils.formatDuration;

public class NowPlayingCommand extends Command {
    private static final String NOW_PLAYING_FORMAT = "Currently playing **%s** by **%s** `[%s/%s]`";

    public NowPlayingCommand() {
        super("nowplaying", "current", "now", "np");
        use(Middlewares.MUST_BE_PLAYING);
    }

    @Override
    public void on(Context context) {
        Song current = context.server.getCurrentSong();
        context.reply(String.format(NOW_PLAYING_FORMAT, current.getTitle(), current.getAuthor(),
                formatDuration(current.getPosition()), formatDuration(current.getDuration())));
    }
}
