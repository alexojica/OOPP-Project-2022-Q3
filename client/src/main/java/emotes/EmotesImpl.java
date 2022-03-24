package emotes;

import javafx.scene.control.MenuItem;

import javax.inject.Inject;
import java.util.List;

public class EmotesImpl implements Emotes{

    private final List<MenuItem> menuItems = List.of(new MenuItem("smile", null),
            new MenuItem(new String(Character.toChars(0x1F480)), null),
            new MenuItem(new String(Character.toChars(0x1F44B)), null),
            new MenuItem(new String(Character.toChars(0x1F6C0)), null)
    );

    @Inject
    public EmotesImpl() {
    }

    public List<MenuItem> getEmotesList() {
        return menuItems;
    }

    public void sendEmote(){

    }
}
