package ovh.not.javamusicbot.lib;

import net.dv8tion.jda.core.entities.VoiceChannel;

interface ConnectionController extends ServerController {
    void connect(VoiceChannel voiceChannel) throws AlreadyConnectedException, PermissionException;

    void disconnect();

    boolean isConnected();
}
