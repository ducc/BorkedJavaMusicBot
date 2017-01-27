package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;

public class RepeatCommand extends Command {
    public RepeatCommand() {
        super("repeat", "loop");
    }

    @Override
    public void on(Context context) {
        if (!context.server.isPlaying()) {
            context.reply("No music is playing on this guild!");
            return;
        }
        //boolean repeat = !musicManager.scheduler.repeat;
        //musicManager.scheduler.repeat = repeat;
        //context.reply("**" + (repeat ? "Enabled" : "Disabled") + "** song repeating!");
    }
}
