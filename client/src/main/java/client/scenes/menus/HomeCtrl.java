package client.scenes.menus;

import client.avatar.AvatarManager;
import client.data.ClientData;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Player;
import exceptions.InvalidServerException;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import javax.inject.Inject;

public class HomeCtrl {

    private final ServerUtils server;
    private final ClientUtils clientUtils;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;
    private final AvatarManager avatarGenerator;

    @FXML
    private TextField name;

    @FXML
    private TextField serverTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private ImageView avatarImage;

    @FXML
    private Text incorrectServerText;

    @Inject
    public HomeCtrl(ServerUtils server, MainCtrl mainCtrl, ClientData clientData, AvatarManager avatarGenerator,
                    ClientUtils clientUtils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.clientData = clientData;
        this.avatarGenerator = avatarGenerator;
        this.clientUtils = clientUtils;
    }

    /**
     * When home screen is shown, this method is called
     */
    public void load()
    {
        setRandomInitName();
        avatarGenerator.setNameAndAvatarImage(name, avatarImage);
        avatarGenerator.initAvatar();
        avatarGenerator.setAvatarImage();
    }

    /**
     * Sets a random name to the prompt text of the player name-selection
     * Name should be generated from a random selection of names (ex MonkeyEye64, KingTower12...)
     */
    public void setRandomInitName()
    {
        if(clientData.getClientPlayer() == null) {
            name.setText("testPlayer");
        }
        else
        {
            name.setText(clientData.getClientPlayer().getName());
        }
    }

    /**
     * Method that is called when the play button is pressed, it:
     * a) Adds the player to the server and
     * b) Sets the avatar chosen from the user
     */
    public void play(){
        String host = serverTextField.getText();
        int port = Integer.parseInt(portTextField.getText());
        try {
            incorrectServerText.setText("");
            server.setHostAndPort(host, port);
        }catch(InvalidServerException e){
            serverTextField.clear();
            portTextField.clear();
            incorrectServerText.setText("Can't find that server!");
        }
        try
        {
            Player p = getPlayer();
            Player serverPlayer = server.addPlayer(p);

            //store client player info received from the server
            clientData.setPlayer(serverPlayer);

            //update the avatar chosen to the specified path (String)
            avatarGenerator.setAvatarOnClient();
        }
        catch (WebApplicationException e)
        {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        mainCtrl.showGameModeSelection();
    }


    private Player getPlayer()
    {
        String userName = name.getText();
        if(userName.length() == 0)
            userName = "testUserX";
        var p = new Player(userName);

        return p;
    }

    public void incrementSeed(){
        avatarGenerator.incrementSeed();
    }

    public void decrementSeed(){
        avatarGenerator.decrementSeed();
    }
}
