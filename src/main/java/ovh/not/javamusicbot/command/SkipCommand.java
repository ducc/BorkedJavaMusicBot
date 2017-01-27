package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;

public class SkipCommand extends Command {
    public SkipCommand() {
        super("skip", "s", "next");
    }

    @Override
    public void on(Context context) {
        if (!context.server.isPlaying()) {
            context.reply("No music is playing on this guild!");
            return;
        }
        context.server.next();
    }
}
