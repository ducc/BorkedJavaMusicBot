package ovh.not.javamusicbot;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Command {
    private static final Pattern FLAG_PATTERN = Pattern.compile("\\s+-([a-zA-Z]+)");
    public final String[] names;
    public boolean hide = false;

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

        public Set<String> parseFlags() {
            String content = String.join(" ", args);
            Matcher matcher = FLAG_PATTERN.matcher(content);
            Set<String> matches = new HashSet<>();
            while (matcher.find()) {
                matches.add(matcher.group().replaceFirst("\\s+-", ""));
            }
            content = content.replaceAll("\\s+-([a-zA-Z]+)", "");
            args = content.split("\\s+");
            return matches;
        }
    }
}
