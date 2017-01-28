package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.Middlewares;
import ovh.not.javamusicbot.lib.song.Song;

import static ovh.not.javamusicbot.Utils.formatDuration;

public class RestartCommand extends Command {
    public RestartCommand() {
        super("restart");
        use(Middlewares.MUST_BE_PLAYING);
    }

    @Override
    public void on(Context context) {
        Song current = context.server.getCurrentSong();
        current.setPosition(0);
        context.reply(String.format("Restarted **%s** by **%s** `[%s]`", current.getTitle(),
                current.getAuthor(), formatDuration(current.getDuration())));
    }
}
