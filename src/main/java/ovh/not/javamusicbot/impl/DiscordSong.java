package ovh.not.javamusicbot.impl;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import ovh.not.javamusicbot.lib.song.Song;

abstract class DiscordSong implements Song {
    private final String id;
    final AudioTrack audioTrack;

    DiscordSong(AudioTrack audioTrack) {
        this.id = null; // TODO sql insert
        this.audioTrack = audioTrack;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getSource() {
        return audioTrack.getSourceManager().getSourceName();
    }

    @Override
    public String getIdentifier() {
        return audioTrack.getIdentifier();
    }

    @Override
    public long getPosition() {
        return audioTrack.getPosition();
    }

    @Override
    public void setPosition(long position) {
        audioTrack.setPosition(position);
    }

    @Override
    public long getDuration() {
        return audioTrack.getDuration();
    }

    @Override
    public String getTitle() {
        return audioTrack.getInfo().title;
    }

    @Override
    public String getAuthor() {
        return audioTrack.getInfo().author;
    }
}
