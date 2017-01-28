package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.Middlewares;

public class PauseCommand extends Command {
    public PauseCommand() {
        super("pause", "resume");
        use(Middlewares.MUST_BE_PLAYING);
    }

    @Override
    public void on(Context context) {
        if (!context.server.isPaused()) {
            context.server.pause();
            context.reply("Paused music playback!");
        } else {
            context.server.resume();
            context.reply("Resumed music playback!");
        }
    }
}
