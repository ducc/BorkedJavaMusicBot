package ovh.not.javamusicbot;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;

import java.util.HashMap;
import java.util.Map;

public class GuildMusicManager {
    private static final Map<Guild, GuildMusicManager> guilds = new HashMap<>();

    private final Guild guild;
    public final AudioPlayer player;
    public final TrackScheduler scheduler;
    public final AudioPlayerSendHandler sendHandler;

    public boolean open = false;

    private GuildMusicManager(Guild guild, AudioPlayerManager playerManager) {
        this.guild = guild;
        this.player = playerManager.createPlayer();
        this.scheduler = new TrackScheduler(player);
        this.player.addListener(scheduler);
        this.sendHandler = new AudioPlayerSendHandler(player);
        this.guild.getAudioManager().setSendingHandler(sendHandler);
    }

    public void open(VoiceChannel channel) {
        guild.getAudioManager().openAudioConnection(channel);
        open = true;
    }

    public static GuildMusicManager getOrCreate(Guild guild, AudioPlayerManager playerManager) {
        if (guilds.containsKey(guild)) {
            return guilds.get(guild);
        }
        GuildMusicManager musicManager = new GuildMusicManager(guild, playerManager);
        guilds.put(guild, musicManager);
        return musicManager;
    }
}
