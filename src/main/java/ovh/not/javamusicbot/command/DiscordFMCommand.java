package ovh.not.javamusicbot.command;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import net.dv8tion.jda.core.Permission;
import org.json.JSONArray;
import org.json.JSONObject;
import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.MusicBot;
import ovh.not.javamusicbot.impl.DiscordServer;
import ovh.not.javamusicbot.lib.AlreadyConnectedException;
import ovh.not.javamusicbot.lib.PermissionException;

@SuppressWarnings("ConstantConditions")
public class DiscordFMCommand extends Command {
    private static final String DFM_BASE_URL = "http://temp.discord.fm";
    private static final String DFM_LIBRARIES_URL = DFM_BASE_URL + "/libraries/json";
    private static final String DFM_LIBRARY_URL = DFM_BASE_URL + "/libraries/%s/json";
    private final Library[] libraries;
    private final String usageResponse;

    public DiscordFMCommand() {
        super("discordfm", "dfm");
        JSONArray array = null;
        try {
            array = Unirest.get(DFM_LIBRARIES_URL).header("User-Agent", MusicBot.USER_AGENT).asJson().getBody().getArray();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        this.libraries = new Library[array.length()];
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            libraries[i] = new Library(object);
        }
        StringBuilder builder = new StringBuilder("Uses a song playlist from http://discord.fm\nUsage: `!!!dfm <library>`" +
                "\n\n**Available libraries:**\n");
        for (int i = 0; i < libraries.length; i++) {
            builder.append(libraries[i].name);
            if (i != libraries.length - 1) {
                builder.append(", ");
            }
        }
        this.usageResponse = builder.toString();
    }

    @Override
    public void on(Context context) {
        if (!context.inVoiceChannel()) {
            context.reply("You must be in a voice channel!");
            return;
        }
        if (context.args.length == 0) {
            context.reply(usageResponse);
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
        String libraryName = String.join(" ", context.args);
        Library library = null;
        for (Library lib : libraries) {
            if (lib.name.equalsIgnoreCase(libraryName)) {
                library = lib;
                break;
            }
        }
        if (library == null) {
            context.reply("Invalid library! Use `!!!dfm` to see usage & libraries.");
            return;
        }
        String[] songs;
        try {
            songs = library.songs();
        } catch (UnirestException e) {
            e.printStackTrace();
            context.reply("An error occurred!");
            return;
        }
        if (songs == null) {
            context.reply("An error occurred!");
            return;
        }
        context.server.getSongQueue().clear();
        // TODO musicManager.scheduler.repeat = false;
        context.server.stop();
        // TODO handler.verbose = false;
        if (!context.server.isConnected()) {
            try {
                context.server.connect(context.getVoiceChannel());
            } catch (AlreadyConnectedException | PermissionException e) {
                context.handleException(e);
                return;
            }
        }
        for (String song : songs) {
            context.server.load(song);
        }
    }

    private class Library {
        private final String id, name;

        private Library(JSONObject json) {
            this.id = json.getString("id");
            this.name = json.getString("name");
        }

        private String[] songs() throws UnirestException {
            String url = String.format(DFM_LIBRARY_URL, id);
            JSONArray array = Unirest.get(url).header("User-Agent", MusicBot.USER_AGENT).asJson().getBody().getArray();
            String[] songs = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                songs[i] = array.getJSONObject(i).getString("identifier");
            }
            return songs;
        }
    }
}
