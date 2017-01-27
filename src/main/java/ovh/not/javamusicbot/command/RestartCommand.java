package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.lib.Song;

import static ovh.not.javamusicbot.Utils.formatDuration;

public class RestartCommand extends Command {
    public RestartCommand() {
        super("restart");
    }

    @Override
    public void on(Context context) {
        if (!context.server.isPlaying()) {
            context.reply("No music is playing on this guild!");
            return;
        }
        Song current = context.server.getCurrentSong();
        current.setPosition(0);
        context.reply(String.format("Restarted **%s** by **%s** `[%s]`", current.getTitle(),
                current.getAuthor(), formatDuration(current.getDuration())));
    }
}
