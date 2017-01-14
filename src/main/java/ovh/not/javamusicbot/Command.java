package ovh.not.javamusicbot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {
    public final String[] names;

    protected Command(String... names) {
        this.names = names;
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
