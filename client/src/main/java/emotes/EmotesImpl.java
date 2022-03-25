package emotes;

import client.data.ClientData;
import client.utils.ServerUtils;
import commons.WebsocketMessage;
import javafx.scene.control.MenuItem;

import javax.inject.Inject;
import java.util.List;

public class EmotesImpl implements Emotes{

    private ServerUtils server;
    private ClientData clientData;

    private final List<MenuItem> menuItems = List.of(
            new MenuItem(new String(Character.toChars(0x1F35D)), null),
            new MenuItem(new String(Character.toChars(0x1F480)), null),
            new MenuItem(new String(Character.toChars(0x1F44B)), null),
            new MenuItem(new String(Character.toChars(0x1F6C0)), null)
    );

    @Inject
    public EmotesImpl(ServerUtils server, ClientData clientData) {
        this.server = server;
        this.clientData = clientData;
    }

    public List<MenuItem> getEmotesList() {
        return menuItems;
    }

    public void sendEmote(String emote) {
        server.send("/app/updateMessages", new WebsocketMessage(
                clientData.getClientPlayer().getName() + ": " + emote,
                clientData.getClientLobby().getToken()));
    }
}
