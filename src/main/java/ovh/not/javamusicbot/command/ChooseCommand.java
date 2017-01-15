package ovh.not.javamusicbot.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;
import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.CommandManager;
import ovh.not.javamusicbot.Selection;

public class ChooseCommand extends Command {
    private final CommandManager commandManager;

    public ChooseCommand(CommandManager commandManager) {
        super("choose", "pick", "select", "cancel");
        this.commandManager = commandManager;
    }

    @Override
    public void on(Context context) {
        Member member = context.event.getMember();
        if (!commandManager.selectors.containsKey(member)) {
            context.reply("You do not have a selector in this guild!");
            return;
        }
        Selection<AudioTrack, String> selection = commandManager.selectors.get(member);
        if (context.args.length == 0) {
            commandManager.selectors.remove(member);
            selection.callback.accept(false, null);
            return;
        }
        switch (context.args[0].toLowerCase()) {
            case "c":
            case "cancel":
                commandManager.selectors.remove(member);
                selection.callback.accept(false, null);
                return;
        }
        int selected;
        try {
            selected = Integer.parseInt(context.args[0]);
        } catch (NumberFormatException e) {
            context.reply(String.format("Invalid input `%s`. Must be an integer with the range 1 - %d. **To cancel selection**, "
                    + "use `!!!cancel`.", context.args[0], selection.items.length));
            return;
        }
        if (selected < 1 || selected > selection.items.length) {
            context.reply(String.format("Invalid input `%s`. Must be an integer with the range 1 - %d. **To cancel selection**, "
                    + "use `!!!cancel`.", context.args[0], selection.items.length));
            return;
        }
        AudioTrack track = selection.items[selected - 1];
        commandManager.selectors.remove(member);
        selection.callback.accept(true, track);
    }
}
