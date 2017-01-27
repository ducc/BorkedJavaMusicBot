package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;

public class StopCommand extends Command {
    public StopCommand() {
        super("stop", "leave", "clear");
    }

    @Override
    public void on(Context context) {
        context.server.stop();
        context.server.getSongQueue().clear();
        context.server.disconnect();
        context.reply("Stopped playing music & left the voice channel.");
    }
}
