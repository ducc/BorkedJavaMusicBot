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
    protected void on(Context context) {
        if (context.argsLength() == 0) {
            return;
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreate(context.event.getGuild(), playerManager);
        playerManager.loadItem(String.join(" ", context.args), new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
                context.reply(String.format("Queued **%s**", audioTrack.getInfo().title));
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                if (audioPlaylist.getSelectedTrack() != null) {
                    trackLoaded(audioPlaylist.getSelectedTrack());
                } else if (audioPlaylist.isSearchResult()) {
                    trackLoaded(audioPlaylist.getTracks().get(0));
                } else {
                    audioPlaylist.getTracks().forEach(musicManager.scheduler::queue);
                }
            }

            @Override
            public void noMatches() {
                context.reply("No song matches found!");
            }

            @Override
            public void loadFailed(FriendlyException e) {
                e.printStackTrace();
                context.reply("An error occurred!");
            }
        });
        if (!musicManager.open) {
            VoiceChannel channel = context.event.getMember().getVoiceState().getChannel();
            musicManager.open(channel);
        }
    }
}
