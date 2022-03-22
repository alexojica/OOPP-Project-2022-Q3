package client.scenes.menus;

import client.avatar.AvatarSupplier;
import client.data.ClientData;
import client.game.Game;
import client.joker.JokerUtils;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ClientUtilsImpl;
import client.utils.ServerUtils;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.eightbit.EightBitAvatar;
import commons.Lobby;
import commons.Player;
import commons.WebsocketMessage;
import constants.ConnectionStatusCodes;
import constants.ResponseCodes;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.inject.Inject;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaitingCtrl implements Initializable{

    private final ServerUtils server;
    private final ClientUtils client;
    private final ClientData clientData;
    private final Game game;

    private final MainCtrl mainCtrl;
    private ObservableList<Player> playerData;

    @FXML
    private TableView<Player> tableView;
    @FXML
    private TableColumn<Player, String> avatarColumn;
    @FXML
    private TableColumn<Player, String> usernameColumn;


    @FXML
    private Text tip;

    @FXML
    private Text lobbyCode;

    private List<Player> activePlayers;

    private Timer timer;
    private Avatar builder;

    private JokerUtils jokerUtils;

    @Inject
    public WaitingCtrl(ServerUtils server, MainCtrl mainCtrl, ClientUtils client, ClientData clientData,
                       JokerUtils jokerUtils, Game game) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.client = client;
        this.clientData = clientData;
        this.jokerUtils = jokerUtils;
        this.game = game;
    }

    /**
     * Method that sets up how the scene should look like when switched to
     */
    public void load(){
        clientData.setQuestionCounter(0);
        tip.setText("Theres only one correct answer per question, get the most right to win.");
        lobbyCode.setText(lobbyCode.getText() + 59864);
        builder = EightBitAvatar.newMaleAvatarBuilder().build();
        showActivePlayers();

        if(client.getClass().equals(ClientUtilsImpl.class)) {
            client.setCurrentSceneCtrl(this);
        }
        server.registerForMessages("/topic/lobbyStart", a -> {
            if(a.getCode() == ResponseCodes.START_GAME && a.getLobbyToken().equals(clientData.getClientLobby().token)) {
                System.out.println("ishost:" + clientData.getIsHost());
                if(clientData.getIsHost())
                    server.send("/app/nextQuestion",
                            new WebsocketMessage(ResponseCodes.NEXT_QUESTION,
                                    clientData.getClientLobby().token, clientData.getClientPointer()));
                jokerUtils.registerForJokerUpdates();
            }
        });

        server.send("/app/requestUpdate",
                new WebsocketMessage(ResponseCodes.LOBBY_UPDATED, clientData.getClientLobby().getToken()));
    }

    /**
     * Method that shows active players in a given lobby
     * The lobby field from CliendData should have been filled/updated prior to calling this method
     */
    public void showActivePlayers()
    {
        activePlayers = clientData.getClientLobby().getPlayersInLobby();
        refresh();
    }

    // Big credits to "https://codereview.stackexchange.com/questions/220969/javafx-lazy-loading-of-images-in-tableview"
    //for the idea of using executor services to schedule the load and/or generation of images
    //it's the only somewhat efficient solution I found, since it makes use of
    // listeners to cancel and monitor on-going tasks
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        usernameColumn.setCellValueFactory(q ->
                new SimpleStringProperty(q.getValue().name));

        ExecutorService exec = Executors.newCachedThreadPool();

        avatarColumn.setCellFactory(param -> {

            //Set up the ImageView
            final ImageView imageview = new ImageView();
            imageview.setFitWidth(35);
            imageview.setPreserveRatio(true);

            //Loading task
            ObjectProperty<Task<Image>> loadingTask = new SimpleObjectProperty<>();

            //Set up the Table
            TableCell<Player, String> cell = new TableCell<Player, String>() {
                @Override
                public void updateItem(String itemCode, boolean empty) {
                    //Stop already running image fetch tast
                    if (loadingTask.get() != null &&
                            loadingTask.get().getState() != Worker.State.SUCCEEDED &&
                            loadingTask.get().getState() != Worker.State.FAILED) {

                        loadingTask.get().cancel();
                    }
                    loadingTask.set(null);
                    //Load image if not null
                    if (empty || itemCode == null) {
                        imageview.setVisible(false);
                    } else {
                        imageview.setVisible(true);
                        Task<Image> task = new Task<Image>() {
                            @Override
                            public Image call() throws Exception {

                                //generate the image
                                Path newPath = AvatarSupplier.generateAvatar(builder, itemCode, null);
                                Image image = new Image(newPath.toString());

                                return image;
                            }
                        };
                        loadingTask.set(task);
                        task.setOnSucceeded(event -> {
                            imageview.setImage(task.getValue());
                        });
                        exec.submit(task);
                    }
                }
            };
            // Attach the imageview to the cell
            cell.setGraphic(imageview);
            return cell;
        });
        avatarColumn.setCellValueFactory(cellData ->  new SimpleStringProperty(cellData.getValue().getAvatarCode()));

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, 0, 250);
    }


    /**
     * Refresh waiting screen page
     */
    public void refresh()
    {
        if(activePlayers != null && clientData.getClientPlayer() != null)
        {
            Lobby current = clientData.getClientLobby();
            clientData.setLobby(current);
            activePlayers = current.getPlayersInLobby();
            playerData = FXCollections.observableList(activePlayers);
            tableView.setItems(playerData);
        }
    }

    public void leaveLobby(){
        client.leaveLobby();
    }

    public void startGame(){
        game.startMultiplayerGame();
    }
}
