package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.CommandManager;
import ovh.not.javamusicbot.Config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdminCommand extends Command {
    private final Map<String, Command> subCommands = new HashMap<>();
    private final Config config;
    private final String subCommandsString;

    public AdminCommand(Config config) {
        super("admin");
        hide = true;
        this.config = config;
        CommandManager.register(subCommands, new StopCommand());
        StringBuilder builder = new StringBuilder("Subcommands:");
        subCommands.values().forEach(command -> builder.append(" ").append(command.names[0]));
        subCommandsString = builder.toString();
    }

    @Override
    public void on(Context context) {
        if (!context.event.getAuthor().getId().equals(config.owner)) {
            return;
        }
        if (context.args.length == 0) {
            context.reply(subCommandsString);
            return;
        }
        if (!subCommands.containsKey(context.args[0])) {
            context.reply("Invalid subcommand!");
            return;
        }
        Command command = subCommands.get(context.args[0]);
        context.args = Arrays.copyOfRange(context.args, 1, context.args.length);
        command.on(context);
    }

    private class StopCommand extends Command {
        private StopCommand() {
            super("stop");
        }

        @Override
        public void on(Context context) {
            context.event.getJDA().shutdown();
            System.exit(0);
        }
    }
}
