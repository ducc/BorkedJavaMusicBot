package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;

import java.util.Collections;
import java.util.List;

public class ShuffleCommand extends Command {
    public ShuffleCommand() {
        super("shuffle");
    }

    @Override
    public void on(Context context) {
        if (!context.server.isPlaying()) {
            context.reply("No music is playing on this guild!");
            return;
        }
        Collections.shuffle((List<?>) context.server.getSongQueue());
        context.reply("Queue shuffled!");
    }
}
