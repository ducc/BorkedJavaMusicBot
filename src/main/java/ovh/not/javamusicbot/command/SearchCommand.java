package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.CommandManager;

public class SearchCommand extends Command {
    private final CommandManager commandManager;

    public SearchCommand(CommandManager commandManager) {
        super("search", "lookup", "youtube", "yt", "find");
        this.commandManager = commandManager;
    }

    @Override
    public void on(Context context) {
        if (context.args.length == 0) {
            context.reply("Usage: `!!!p <link` - plays a song\n" +
                    "To search youtube, use `!!!p ytsearch: <your search term>`");
            return;
        }
        String[] args = new String[context.args.length + 1];
        args[0] = "ytsearch: ";
        System.arraycopy(context.args, 0, args, 1, context.args.length);
        context.args = args;
        commandManager.commands.get("play").on(context);
    }
}
