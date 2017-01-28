package ovh.not.javamusicbot;

public abstract class Middlewares {
    interface Middleware {
        boolean on(Command.Context context);
    }

    public static final Middleware MUST_BE_PLAYING = context -> {
        if (!context.server.isPlaying()) {
            context.reply("No music is playing on this guild!");
            return true;
        }
        return false;
    };

    public static final Middleware MUST_BE_IN_VOICE_CHANNEL = context -> {
        if (!context.inVoiceChannel()) {
            context.reply("You must be in a voice channel!");
            return true;
        }
        return false;
    };
}
