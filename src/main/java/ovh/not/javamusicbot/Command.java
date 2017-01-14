package ovh.not.javamusicbot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command extends CommandUtils {
    public final String[] names;

    protected Command(String name, String... names) {
        this.names = new String[names.length + 1];
        this.names[0] = name;
        System.arraycopy(names, 0, this.names, 1, names.length);
    }

    public abstract void on(Context context);

    protected class Context {
        public MessageReceivedEvent event;
        public String[] args;

        public Message reply(String message) {
            return event.getChannel().sendMessage(message).complete();
        }
    }
}
