package ovh.not.javamusicbot;

import ovh.not.javamusicbot.command.TestCommand;

import java.util.HashMap;
import java.util.Map;

class CommandManager {
    private final Map<String, Command> commands = new HashMap<>();

    CommandManager() {
        register(
            new TestCommand()
        );
    }

    private void register(Command... commands) {
        for (Command command : commands) {
            for (String name : command.names) {
                if (this.commands.containsKey(name)) {
                    throw new RuntimeException(String.format("Command name collision %s in %s!", name,
                            command.getClass().getName()));
                }
                this.commands.put(name, command);
            }
        }
    }

    Command getCommand(String name) {
        return commands.get(name);
    }
}
