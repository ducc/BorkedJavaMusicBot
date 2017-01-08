package ovh.not.javamusicbot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class Command {
    final String[] names;

    protected Command(String... names) {
        this.names = names;
    }

    protected abstract void on(Context context);

    protected class Context {
        public MessageReceivedEvent event;

        Context(MessageReceivedEvent event) {
            this.event = event;
        }

        public Message reply(String message) {
            return event.getChannel().sendMessage(message).complete();
        }
    }
}
