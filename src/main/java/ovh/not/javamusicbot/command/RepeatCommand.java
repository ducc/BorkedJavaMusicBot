package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.Middlewares;

public class RepeatCommand extends Command {
    public RepeatCommand() {
        super("repeat", "loop");
        use(Middlewares.MUST_BE_PLAYING);
    }

    @Override
    public void on(Context context) {
        //boolean repeat = !musicManager.scheduler.repeat;
        //musicManager.scheduler.repeat = repeat;
        //context.reply("**" + (repeat ? "Enabled" : "Disabled") + "** song repeating!");
    }
}
