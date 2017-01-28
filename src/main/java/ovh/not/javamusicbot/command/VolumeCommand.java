package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;

public class VolumeCommand extends Command {
    public VolumeCommand() {
        super("volume", "v");
        hide = true;
    }

    @Override
    public void on(Context context) {
        context.reply("Changing song volume is known to cause performance issues. The command has been disabled "
                + "until a solution is found.");
    }
}
