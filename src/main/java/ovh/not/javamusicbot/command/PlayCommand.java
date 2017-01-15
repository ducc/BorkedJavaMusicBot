package ovh.not.javamusicbot.command;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.VoiceChannel;
import ovh.not.javamusicbot.Command;
import ovh.not.javamusicbot.GuildMusicManager;

public class PlayCommand extends Command {
    private final AudioPlayerManager playerManager;

    public PlayCommand(AudioPlayerManager playerManager) {
        super("play", "p");
        this.playerManager = playerManager;
    }

    @Override
    public void on(Context context) {
        if (context.args.length == 0) {
            context.reply("Usage: `!!!p <link` - plays a song\n" +
                    "To search youtube, use `!!!p ytsearch: <your search term>`");
            return;
        }
        VoiceChannel channel = context.event.getMember().getVoiceState().getChannel();
        if (channel == null) {
            context.reply("You must be in a voice channel!");
            return;
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreate(context.event.getGuild(),
                context.event.getTextChannel(), playerManager);
        playerManager.loadItem(String.join(" ", context.args), new LoadResultHandler(musicManager, context));
        if (!musicManager.open) {
            musicManager.open(channel);
        }
    }

    private class LoadResultHandler implements AudioLoadResultHandler {
        private final GuildMusicManager musicManager;
        private final Context context;

        LoadResultHandler(GuildMusicManager musicManager, Context context) {
            this.musicManager = musicManager;
            this.context = context;
        }

        @Override
        public void trackLoaded(AudioTrack audioTrack) {
            boolean playing = musicManager.player.getPlayingTrack() != null;
            musicManager.scheduler.queue(audioTrack);
            if (playing) {
                context.reply(String.format("Queued **%s** `[%s]`", audioTrack.getInfo().title,
                        formatDuration(audioTrack.getDuration())));
            }
        }

        @Override
        public void playlistLoaded(AudioPlaylist audioPlaylist) {
            if (audioPlaylist.getSelectedTrack() != null) {
                trackLoaded(audioPlaylist.getSelectedTrack());
            } else if (audioPlaylist.isSearchResult()) {
                trackLoaded(audioPlaylist.getTracks().get(0));
            } else {
                audioPlaylist.getTracks().forEach(musicManager.scheduler::queue);
                context.reply(String.format("Added **%d songs** to the queue!", audioPlaylist.getTracks().size()));
            }
        }

        @Override
        public void noMatches() {
            context.reply("No song matches found! Usage: `!!!p <link>`\n" +
                    "To play music from youtube, use `!!!p ytsearch: <your search term>`");
        }

        @Override
        public void loadFailed(FriendlyException e) {
            e.printStackTrace();
            context.reply("An error occurred!");
        }
    }
}
