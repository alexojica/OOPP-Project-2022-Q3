package client.scenes.menus;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.utils.AvatarSupplier;
import client.utils.ServerUtils;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.eightbit.EightBitAvatar;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;

import javax.inject.Inject;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import commons.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import commons.Lobby;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class HomeCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;

    private final String baseAvatarName = "OOPPP";
    private int seed = 0;
    private Path avatarPath = null;
    private Avatar playerAvatar;

    @FXML
    private TextField name;

    @FXML
    private ImageView avatarImage;

    @Inject
    public HomeCtrl(ServerUtils server, MainCtrl mainCtrl, ClientData clientData) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.clientData = clientData;
    }

    public void play(){
        try
        {
            Player p = getPlayer();

            Player serverPlayer = server.addPlayer(p);

            //store client player info received from the server
            clientData.setPlayer(serverPlayer);

            //update the avatar chosen to the specified path (String)
            renameFile();
            clientData.getClientPlayer().setAvatar(avatarPath.toString());
            clientData.getClientPlayer().setAvatarCode(baseAvatarName + seed + name.getText());
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

    //these methods are called onLoad automatically

    public void onLoad()
    {
        setRandomInitName();
        instantiateCommonLobby();
        initAvatar();
        setAvatarImage();
    }

    public void initAvatar()
    {
        //clear past avatars, only if the client is restarted, otherwise use already generated files
        if(clientData.getClientPlayer() == null) AvatarSupplier.clearAllAvatars();
        playerAvatar = EightBitAvatar.newMaleAvatarBuilder().build();
    }

    public void setAvatarImage()
    {
        if(clientData.getClientPlayer() == null ||
                clientData.getClientPlayer().getAvatar() == null ||
                clientData.getClientPlayer().getAvatar().equals("")
        ){
            //first time generating the resource
            updateAvatar();
            updateImage();
            System.out.println("Avatar image successfully generated at: " + avatarPath);
        }
        else
        {
            //reuse the same file
            avatarPath = Paths.get(clientData.getClientPlayer().getAvatar());
            updateImage();
        }


        //set listener to look for changes
        name.textProperty().addListener(((observable, oldValue, newValue) -> {
            avatarPath = AvatarSupplier.generateAvatar(playerAvatar,newValue,avatarPath);
            updateImage();
        }));
    }

    /**
     * Method that updates the avatar based on a given inputName, from which it will construct the seed
     * If the avatarPath is null(first pass), it will assign it a new path, and
     * then only change the given image in that path
     */
    public void updateAvatar()
    {
        String inputName = baseAvatarName + seed + name.getText();

        avatarPath = AvatarSupplier.generateAvatar(playerAvatar, inputName, avatarPath);
    }

    public void updateImage()
    {
        try{
            avatarImage.setImage(new Image(Files.newInputStream(avatarPath)));
        }catch (IOException  | NullPointerException e)
        {
            System.out.println("Failed to set image");
        }

    }

    private void renameFile()
    {
        String inputName = baseAvatarName + seed + name.getText();
        avatarPath = AvatarSupplier.renameAvatarFile(avatarPath,inputName);
    }

    public void incrementSeed()
    {
        seed ++;
        updateAvatar();
        updateImage();
    }

    public void decrementSeed()
    {
        seed --;
        updateAvatar();
        updateImage();
    }

    public void setRandomInitName()
    {
        //this string should be randomly generated
        //from a pool of possible name combinations
        // ex: MonkeyEye64, KingTower12 etc

        if(clientData.getClientPlayer() == null) {
            this.name.setText("testPlayer");
        }
        else
        {
            this.name.setText(clientData.getClientPlayer().getName());
        }
    }

    public void instantiateCommonLobby()
    {
        //this code should be private static string final somewhere
        String commonCode = "COMMON";

        List<Lobby> lobbies = server.getAllLobbies();

        if(lobbies.size() == 0)
        {
            //no lobbies instantiated

            Lobby mainLobby = new Lobby(commonCode);
            server.addLobby(mainLobby);
            System.out.println("Lobby created");
        }
        else
        {
            //lobbies exist, but there might not be any common lobby
            //TASK: improve the search of lobbies; maybe server sided, not client sided

            boolean commonLobbyExists = false;
            for(Lobby l : lobbies)
            {
                if(l.getToken().equals(commonCode))
                    commonLobbyExists = true;
            }

            if(!commonLobbyExists)
            {
                Lobby mainLobby = new Lobby(commonCode);
                server.addLobby(mainLobby);
            }
        }
    }

}
