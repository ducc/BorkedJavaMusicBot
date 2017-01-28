package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.Middlewares;

import java.util.Collections;
import java.util.List;

public class ShuffleCommand extends Command {
    public ShuffleCommand() {
        super("shuffle");
        use(Middlewares.MUST_BE_PLAYING);
    }

    @Override
    public void on(Context context) {
        Collections.shuffle((List<?>) context.server.getSongQueue());
        context.reply("Queue shuffled!");
    }
}
