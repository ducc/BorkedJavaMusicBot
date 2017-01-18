package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;

public class GuildsCommand extends Command {
    public GuildsCommand() {
        super("guilds");
    }

    @Override
    public void on(Context context) {
        context.reply("Guilds: " + context.event.getJDA().getGuilds().size());
    }
}
