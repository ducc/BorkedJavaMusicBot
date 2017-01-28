package ovh.not.javamusicbot.command;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.Middlewares;
import ovh.not.javamusicbot.lib.song.Song;

import java.util.ArrayList;
import java.util.List;

import static ovh.not.javamusicbot.MusicBot.GSON;
import static ovh.not.javamusicbot.Utils.HASTEBIN_URL;

public class DumpCommand extends Command {
    public DumpCommand() {
        super("dump");
        use(Middlewares.MUST_BE_PLAYING);
    }

    @Override
    public void on(Context context) {
        List<DumpItem> items = new ArrayList<>();
        Song current = context.server.getCurrentSong();
        items.add(new DumpItem(0, current.getSource(), current.getIdentifier()));
        int i = 1;
        for (Song song : context.server.getSongQueue().get()) {
            items.add(new DumpItem(i, song.getSource(), song.getIdentifier()));
            i++;
        }
        String json = GSON.toJson(items);
        Unirest.post(HASTEBIN_URL).body(json).asJsonAsync(new Callback<JsonNode>() {
            @Override
            public void completed(HttpResponse<JsonNode> httpResponse) {
                context.reply(String.format("Dump created! https://hastebin.com/%s.json", httpResponse.getBody()
                        .getObject().getString("key")));
            }

            @Override
            public void failed(UnirestException e) {
                e.printStackTrace();
                context.reply("An error occured!");
            }

            @Override
            public void cancelled() {
                context.reply("Operation cancelled.");
            }
        });
    }

    @SuppressWarnings("unused")
    private class DumpItem {
        private int index;
        private String source;
        private String identifier;

        private DumpItem(int index, String source, String identifier) {
            this.index = index;
            this.source = source;
            this.identifier = identifier;
        }
    }
}
