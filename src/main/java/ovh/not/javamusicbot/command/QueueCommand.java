package ovh.not.javamusicbot.command;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.Pageable;
import ovh.not.javamusicbot.lib.Song;

import java.util.List;

import static ovh.not.javamusicbot.Utils.*;

@SuppressWarnings("unchecked")
public class QueueCommand extends Command {
    private static final String BASE_LINE = "%s by %s `[%s]`";
    private static final String CURRENT_LINE = "__Currently playing:__\n" + BASE_LINE;
    private static final String QUEUE_LINE = "\n`%02d` " + BASE_LINE;
    private static final String SONG_QUEUE_LINE = "\n\n__Song queue:__ (Page **%d** of **%d**)";
    private static final int PAGE_SIZE = 10;

    public QueueCommand() {
        super("queue", "list", "q");
    }

    @Override
    public void on(Context context) {
        if (!context.server.isPlaying()) {
            context.reply("No music is queued or playing on this guild!");
            return;
        }
        Song playing = context.server.getCurrentSong();
        List<Song> queue = (List<Song>) context.server.getSongQueue().get();
        StringBuilder builder = new StringBuilder();
        if (context.args.length > 0 && context.args[0].equalsIgnoreCase("all")) {
            long durationTotal = playing.getDuration();
            StringBuilder items = new StringBuilder();
            for (int i = 0; i < queue.size(); i++) {
                Song song = queue.get(i);
                durationTotal += song.getDuration();
                items.append(String.format("\n%02d %s by %s [%s]", i + 1, song.getTitle(),
                        song.getAuthor(), formatDuration(song.getDuration())));
            }
            builder.append(String.format("Song queue for %s - %d songs (%s).\nCurrent song: %s by %s [%s/%s]\n",
                    context.event.getGuild().getName(), queue.size(), formatLongDuration(durationTotal),
                    playing.getTitle(), playing.getAuthor(), formatDuration(playing.getPosition()),
                    formatDuration(playing.getDuration())));
            builder.append(items.toString());
            Unirest.post(HASTEBIN_URL).body(builder.toString()).asJsonAsync(new Callback<JsonNode>() {
                @Override
                public void completed(HttpResponse<JsonNode> httpResponse) {
                    context.reply(String.format("Full song queue: https://hastebin.com/raw/%s", httpResponse.getBody()
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
        } else {
            builder.append(String.format(CURRENT_LINE, playing.getTitle(), playing.getAuthor(),
                    formatDuration(playing.getPosition()) + "/" + formatDuration(playing.getDuration())));
            Pageable<Song> pageable = new Pageable<>(queue);
            pageable.setPageSize(PAGE_SIZE);
            if (context.args.length > 0) {
                int page;
                try {
                    page = Integer.parseInt(context.args[0]);
                } catch (NumberFormatException e) {
                    context.reply(String.format("Invalid page! Must be an integer within the range %d - %d",
                            pageable.getMinPageRange(), pageable.getMaxPages()));
                    return;
                }
                if (page < pageable.getMinPageRange() || page > pageable.getMaxPages()) {
                    context.reply(String.format("Invalid page! Must be an integer within the range %d - %d",
                            pageable.getMinPageRange(), pageable.getMaxPages()));
                    return;
                }
                pageable.setPage(page);
            } else {
                pageable.setPage(pageable.getMinPageRange());
            }
            builder.append(String.format(SONG_QUEUE_LINE, pageable.getPage(), pageable.getMaxPages()));
            int index = 1;
            for (Song song : pageable.getListForPage()) {
                builder.append(String.format(QUEUE_LINE, ((pageable.getPage() - 1) * pageable.getPageSize()) + index,
                        song.getTitle(), song.getAuthor(), formatDuration(song.getDuration())));
                index++;
            }
            if (pageable.getPage() < pageable.getMaxPages()) {
                builder.append("\n\n__To see the next page:__ `!!!queue ").append(pageable.getPage() + 1)
                        .append("`\nTo see the full queue, use `!!!queue all`");
            }
            context.reply(builder.toString());
        }
    }
}
