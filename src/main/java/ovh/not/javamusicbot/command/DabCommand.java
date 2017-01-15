package ovh.not.javamusicbot.command;

import ovh.not.javamusicbot.Command;

import java.io.File;
import java.io.IOException;

public class DabCommand extends Command {
    private static final File DAB_IMAGE = new File("dab.jpg");

    public DabCommand() {
        super("dab");
    }

    @Override
    public void on(Context context) {
        try {
            context.event.getTextChannel().sendFile(DAB_IMAGE, null).complete();
        } catch (IOException e) {
            e.printStackTrace();
            context.reply("An error occurred!");
        }
    }
}
