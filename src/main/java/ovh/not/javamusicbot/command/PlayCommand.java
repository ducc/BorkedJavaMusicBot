package ovh.not.javamusicbot.command;

import net.dv8tion.jda.core.Permission;
import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.Middlewares;
import ovh.not.javamusicbot.impl.DiscordServer;
import ovh.not.javamusicbot.lib.AlreadyConnectedException;
import ovh.not.javamusicbot.lib.PermissionException;

import java.util.Set;

public class PlayCommand extends Command {
    public PlayCommand() {
        super("play", "p");
        use(Middlewares.MUST_BE_IN_VOICE_CHANNEL);
    }

    @Override
    public void on(Context context) {
        if (context.args.length == 0) {
            context.reply("Usage: `!!!play <link>` - plays a song\n" +
                    "To search youtube, use `!!!play <youtube video title>`\n" +
                    "To add as first in queue, use `!!!play <link> -first`");
            return;
        }
        if (context.server.isPlaying() && ((DiscordServer) context.server).voiceChannel != context.getVoiceChannel()
                && !context.event.getMember().hasPermission(((DiscordServer) context.server).voiceChannel,
                Permission.VOICE_MOVE_OTHERS)) {
            context.reply("dabBot is already playing music in "
                    + ((DiscordServer) context.server).voiceChannel.getName() + " so it cannot be moved. Members " +
                    "with the `VOICE_MOVE_OTHERS` permission are exempt from this.");
            return;
        }
        // TODO handler.allowSearch = true;
        Set<String> flags = context.parseFlags();
        if (flags.contains("first") || flags.contains("f")) {
            // TODO handler.setFirstInQueue = true;
        }
        System.out.println("connected? " + context.server.isConnected());
        if (!context.server.isConnected()) {
            try {
                context.server.connect(context.getVoiceChannel());
            } catch (AlreadyConnectedException | PermissionException e) {
                context.handleException(e);
                return;
            }
        }
        context.server.load(String.join(" ", context.args), context.user);
    }
}
