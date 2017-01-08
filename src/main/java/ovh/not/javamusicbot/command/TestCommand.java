package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;

public class TestCommand extends Command {
    public TestCommand() {
        super("test", "t");
    }

    @Override
    protected void on(Command.Context context) {
        context.reply("Testing, 1 2 3!");
    }
}
