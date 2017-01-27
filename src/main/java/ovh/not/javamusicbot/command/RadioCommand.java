package ovh.not.javamusicbot.command;

import net.dv8tion.jda.core.Permission;
import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.Constants;
import ovh.not.javamusicbot.impl.DiscordServer;
import ovh.not.javamusicbot.lib.AlreadyConnectedException;
import ovh.not.javamusicbot.lib.PermissionException;

import java.util.Iterator;
import java.util.Map;

public class RadioCommand extends Command {
    private final Constants constants;
    private final String usageMessage;

    public RadioCommand(Constants constants) {
        super("radio", "station", "stations", "fm");
        this.constants = constants;
        StringBuilder builder = new StringBuilder("Streams a variety of UK radio stations.\n" +
                "Usage: `!!!radio <station>`\n" +
                "\n**Available stations:**\n");
        Iterator<String> iterator = constants.radioStations.keySet().iterator();
        while (iterator.hasNext()) {
            String station = iterator.next();
            builder.append(station.substring(1, station.length() - 1));
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        this.usageMessage = builder.toString();
    }

    @Override
    public void on(Context context) {
        if (!context.inVoiceChannel()) {
            context.reply("You must be in a voice channel!");
            return;
        }
        if (context.args.length == 0) {
            context.reply(usageMessage);
            return;
        }
        String station = "\"" + String.join(" ", context.args) + "\"";
        String url = null;
        for (Map.Entry<String, String> entry : constants.radioStations.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(station)) {
                url = entry.getValue();
                break;
            }
        }
        if (url == null) {
            context.reply("Invalid station! For usage & stations, use `!!!radio`");
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
        context.server.stop();
        context.server.getSongQueue().clear();
        // TODO musicManager.scheduler.repeat = false;
        if (!context.server.isConnected()) {
            try {
                context.server.connect(context.getVoiceChannel());
            } catch (AlreadyConnectedException | PermissionException e) {
                context.handleException(e);
                return;
            }
        }
        context.server.load(url);
    }
}
