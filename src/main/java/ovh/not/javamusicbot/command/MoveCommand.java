package ovh.not.javamusicbot.command;

import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.Middlewares;
import ovh.not.javamusicbot.impl.DiscordServer;
import ovh.not.javamusicbot.lib.AlreadyConnectedException;
import ovh.not.javamusicbot.lib.PermissionException;

import java.util.List;

public class MoveCommand extends Command {
    public MoveCommand() {
        super("move");
        use(Middlewares.MUST_BE_PLAYING);
    }

    @Override
    public void on(Context context) {
        if (context.server.isPlaying()
                && !context.event.getMember().hasPermission(((DiscordServer) context.server).voiceChannel,
                Permission.VOICE_MOVE_OTHERS)) {
            context.reply("dabBot is already playing music in "
                    + ((DiscordServer) context.server).voiceChannel.getName() + " so it cannot be moved. Members " +
                    "with the `VOICE_MOVE_OTHERS` permission are exempt from this.");
            return;
        }
        if (context.args.length == 0) {
            context.reply("Usage: !!!move <voice channel name>");
            return;
        }
        Guild guild = context.event.getGuild();
        List<VoiceChannel> channels = guild.getVoiceChannelsByName(String.join(" ", context.args), true);
        if (channels == null || channels.size() == 0) {
            context.reply("Could not find the specified voice channel!");
            return;
        }
        VoiceChannel channel = channels.get(0);
        context.server.pause();
        context.server.disconnect();
        try {
            context.server.connect(channel);
        } catch (AlreadyConnectedException | PermissionException e) {
            context.handleException(e);
            return;
        }
        context.server.resume();
        context.reply("Moved voice channel!");
    }
}
