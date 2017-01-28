package ovh.not.javamusicbot.lib.server;

import net.dv8tion.jda.core.entities.VoiceChannel;
import ovh.not.javamusicbot.lib.AlreadyConnectedException;
import ovh.not.javamusicbot.lib.PermissionException;

interface ConnectionController extends ServerController {
    void connect(VoiceChannel voiceChannel) throws AlreadyConnectedException, PermissionException;

    void disconnect();

    boolean isConnected();
}
