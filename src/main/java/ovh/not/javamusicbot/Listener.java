package ovh.not.javamusicbot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Listener extends ListenerAdapter {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("!!!([a-zA-Z]+)\\s+(.*)");

    private final CommandManager commandManager;

    Listener(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Matcher matcher = COMMAND_PATTERN.matcher(event.getMessage().getContent());
        if (!matcher.find()) {
            return;
        }
        String name = matcher.group(1);
        Command command = commandManager.getCommand(name);
        if (command == null) {
            return;
        }
        Command.Context context = command.new Context();
        context.event = event;
        if (matcher.groupCount() > 1) {
            context.args = matcher.group(2).split("\\s+");
        }
        command.on(context);
    }
}
