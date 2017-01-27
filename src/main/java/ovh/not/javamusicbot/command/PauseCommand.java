package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;

public class PauseCommand extends Command {
    public PauseCommand() {
        super("pause", "resume");
    }

    @Override
    public void on(Context context) {
        if (!context.server.isPlaying()) {
            context.reply("No music is playing on this guild!");
            return;
        }
        if (!context.server.isPaused()) {
            context.server.pause();
            context.reply("Paused music playback!");
        } else {
            context.server.resume();
            context.reply("Resumed music playback!");
        }
    }
}
