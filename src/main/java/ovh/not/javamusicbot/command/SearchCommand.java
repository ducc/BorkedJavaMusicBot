package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;

public class SearchCommand extends Command {
    public SearchCommand() {
        super("search", "lookup", "youtube", "yt", "find");
    }

    @Override
    public void on(Context context) {
        context.reply("**The search command is no longer used!** Simply do `!!!play <link or song title>`.");
    }
}
