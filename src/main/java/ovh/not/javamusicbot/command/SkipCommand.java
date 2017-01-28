package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.Middlewares;

public class SkipCommand extends Command {
    public SkipCommand() {
        super("skip", "s", "next");
        use(Middlewares.MUST_BE_PLAYING);
    }

    @Override
    public void on(Context context) {
        context.server.next();
    }
}
