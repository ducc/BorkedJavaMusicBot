package ovh.not.javamusicbot.command;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.core.entities.VoiceChannel;
import ovh.not.javamusicbot.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static ovh.not.javamusicbot.MusicBot.GSON;

public class DiscordFMCommand extends Command {
    private static final String DFM_LIBRARY_URL = "https://temp.discord.fm/libraries/%s/json";

    private final CommandManager commandManager;
    private final AudioPlayerManager playerManager;
    private final String usageResponse;

    public DiscordFMCommand(CommandManager commandManager, AudioPlayerManager playerManager) {
        super("discordfm", "dfm");
        this.commandManager = commandManager;
        this.playerManager = playerManager;
        StringBuilder builder = new StringBuilder("Uses a song playlist from http://discord.fm!\nUsage: `!!!dfm <library>`" +
                "\n\n**Available libraries:**\n");
        Library[] values = Library.values();
        for (int i = 0; i < values.length; i++) {
            builder.append(values[i].name().toLowerCase().replace('_', ' '));
            if (i != values.length - 1) {
                builder.append(", ");
            }
        }
        this.usageResponse = builder.toString();
        // making ourself venerable to mitm attacks
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
            public X509Certificate[] getAcceptedIssuers(){return null;}
            public void checkClientTrusted(X509Certificate[] certs, String authType){}
            public void checkServerTrusted(X509Certificate[] certs, String authType){}
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception ignored) {
        }
    }

    @Override
    public void on(Context context) {
        VoiceChannel channel = context.event.getMember().getVoiceState().getChannel();
        if (channel == null) {
            context.reply("You must be in a voice channel!");
            return;
        }
        if (context.args.length == 0) {
            context.reply(usageResponse);
            return;
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreate(context.event.getGuild(),
                context.event.getTextChannel(), playerManager);
        String libraryName = String.join(" ", context.args).toUpperCase().replace(' ', '_');
        Library library;
        try {
            library = Library.valueOf(libraryName);
        } catch (IllegalArgumentException e) {
            context.reply("Invalid library! See `!!!dfm` for usage & libraries.");
            return;
        }
        List<String> songs;
        try {
            songs = library.get();
        } catch (UnirestException e) {
            e.printStackTrace();
            context.reply("An error occurred!");
            return;
        }
        if (songs == null) {
            context.reply("An error occurred!");
            return;
        }
        musicManager.scheduler.queue.clear();
        musicManager.scheduler.repeat = false;
        musicManager.player.stopTrack();
        LoadResultHandler handler = new LoadResultHandler(commandManager, musicManager, context);
        handler.verbose = false;
        songs.forEach(song -> playerManager.loadItem(song, handler));

    }

    private enum Library {
        ELECTRO_HUB, CHILL_CORNER, KOREAN_MADNESS, JAPANESE_LOUNGE, CLASSICAL, RETRO_RENEGADE, METAL_MIX, HIP_HOP,
        ROCK_N_ROLL, COFFEE_HOUSE_JAZZ;

        private final String urlName;

        Library() {
            this.urlName = this.name().toLowerCase().replace("_", "-");
        }

        private List<String> get() throws UnirestException {
            String url = String.format(DFM_LIBRARY_URL, urlName);
            HttpsURLConnection connection;
            try {
                connection = (HttpsURLConnection) new URL(url).openConnection();
                connection.addRequestProperty("User-Agent", MusicBot.USER_AGENT);
                connection.setRequestMethod("GET");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            List<String> songs = new ArrayList<>();
            JsonArray json;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                json = GSON.fromJson(builder.toString(), JsonArray.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            json.forEach(element -> {
                JsonObject object = element.getAsJsonObject();
                songs.add(object.get("identifier").getAsString());
            });
            return songs;
        }
    }
}
