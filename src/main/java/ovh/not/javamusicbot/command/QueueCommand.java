package ovh.not.javamusicbot.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.GuildMusicManager;
import ovh.not.javamusicbot.Pageable;

import java.util.List;
import java.util.Queue;

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
        GuildMusicManager musicManager = GuildMusicManager.get(context.event.getGuild());
        if (musicManager == null || musicManager.player.getPlayingTrack() == null) {
            context.reply("No music is queued or playing on this guild!");
            return;
        }
        AudioTrack playing = musicManager.player.getPlayingTrack();
        Queue<AudioTrack> queue = musicManager.scheduler.queue;
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(CURRENT_LINE, playing.getInfo().title, playing.getInfo().author,
                formatDuration(playing.getPosition()) + "/" + formatDuration(playing.getDuration())));
        Pageable<AudioTrack> pageable = new Pageable<>((List<AudioTrack>) queue);
        pageable.setPageSize(PAGE_SIZE);
        if (context.args.length > 0) {
            int page;
            try {
                page = Integer.parseInt(context.args[0]);
            } catch (NumberFormatException e) {
                context.reply(String.format("Invalid page! Must be an integer within the range %d - %d",
                        pageable.getMinPageRange(), pageable.getMaxPageRange()));
                return;
            }
            if (page < pageable.getMinPageRange() || page > pageable.getMaxPageRange()) {
                context.reply(String.format("Invalid page! Must be an integer within the range %d - %d",
                        pageable.getMinPageRange(), pageable.getMaxPageRange()));
                return;
            }
            pageable.setPage(page);
        } else {
            pageable.setPage(pageable.getMinPageRange());
        }
        builder.append(String.format(SONG_QUEUE_LINE, pageable.getPage(), pageable.getMaxPageRange()));
        int index = 1;
        for (AudioTrack track : pageable.getListForPage()) {
            builder.append(String.format(QUEUE_LINE, ((pageable.getPage() - 1) * pageable.getPageSize()) + index, track.getInfo().title, track.getInfo().author,
                    formatDuration(track.getDuration())));
            index++;
        }
        if (pageable.getPage() < pageable.getMaxPageRange()) {
            builder.append("\n\n__To see the next page:__ `!!!queue ").append(pageable.getPage() + 1).append("`");
        }
        context.reply(builder.toString());
    }
}
