package client.scenes.menus;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.utils.AvatarSupplier;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.eightbit.EightBitAvatar;
import commons.Lobby;
import commons.Player;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.io.File;
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

    @Inject
    public WaitingCtrl(ServerUtils server, MainCtrl mainCtrl, ClientUtils client, ClientData clientData) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.client = client;
        this.clientData = clientData;
    }

    /**
     * Method that sets up how the scene should look like when switched to
     */
    public void load(){
        tip.setText("Theres only one correct answer per question, get the most right to win.");
        lobbyCode.setText(lobbyCode.getText() + 59864);
        builder = EightBitAvatar.newMaleAvatarBuilder().build();
        showActivePlayers();
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
    //it's the only somewhat efficient solution I found, since it makes use of listeners to cancel and monitor on-going tasks

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        usernameColumn.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().name));

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

    public boolean isInLobby()
    {
        if(clientData.getClientLobby() == null) return false;
        return true;
    }

    public void refresh()
    {
        if(activePlayers != null && isInLobby())
        {
            String token = clientData.getClientLobby().getToken();
            Lobby current = server.getLobbyByToken(token);
            clientData.setLobby(current);
            activePlayers = current.getPlayersInLobby();
            playerData = FXCollections.observableList(activePlayers);
            tableView.setItems(playerData);

            //check if game has started in this lobby
            if(current.getStarted()) {
                timer.cancel();
                Platform.runLater(() -> initiateGame());
                //initiateGame();
                System.out.println("Game started in lobby " + token);
            }
        }
    }

    public void leaveLobby(){
        client.leaveLobby();
    }

    public void initiateGame()
    {
        clientData.setPointer(clientData.getClientLobby().getPlayerIds().get(0));
        clientData.setClientScore(0L);
        clientData.setQuestionCounter(0);

        //add delay until game starts
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //TODO: add timer progress bar / UI text with counter depleting until the start of the game
                    Thread.sleep(3000);

                    //prepare the question again only if not host
                    if(!clientData.getIsHost()) client.prepareQuestion();
                    Platform.runLater(() -> client.getQuestion());

                }catch (InterruptedException e){
                    e.printStackTrace();
                    System.out.println("Something went wrong while waiting to start the game");
                }
            }
        });
        thread.start();
    }

    //Only one player presses start game
    //that player now becomes the HOST
    //the HOST precalculates the question - only one api call
    //the rest use the pregenerated question

    public void startGame(){
        //start the game for the other players as well
        String token = clientData.getClientLobby().getToken();
        server.startLobby(token);

        clientData.setPointer(clientData.getClientLobby().getPlayerIds().get(0));
        clientData.setClientScore(0L);
        clientData.setQuestionCounter(0);
        clientData.setAsHost(true);

        client.prepareQuestion();
    }
}
