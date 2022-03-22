package client.scenes;

import client.data.ClientData;
import client.utils.AvatarSupplier;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.eightbit.EightBitAvatar;
import commons.Player;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameOverCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;

    private ObservableList<Player> currentTop10;
    private Avatar builder;

    @FXML
    private TableView<Player> table;
    @FXML
    private TableColumn rank;
    @FXML
    private TableColumn<Player, String> nameColumn;
    @FXML
    private TableColumn<Player, String> avatarColumn;
    @FXML
    private TableColumn<Player, Integer> scoreColumn;

    @Inject
    public GameOverCtrl(ServerUtils server, MainCtrl mainCtrl, ClientData clientData) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.clientData = clientData;
    }

    public void playAgain() {
        mainCtrl.showGameModeSelection();
    }

    public void leaveGame() {
        mainCtrl.showHome();
    }

    public void load() {
        currentTop10 = FXCollections.observableList(server.getTopByLobbyToken(clientData.getClientLobby().getToken()));
        builder = EightBitAvatar.newMaleAvatarBuilder().build();

        rank.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Player, String>, ObservableValue<String>>() {
            @Override public ObservableValue<String> call(TableColumn.CellDataFeatures<Player, String> p) {
                return new ReadOnlyObjectWrapper((table.getItems().indexOf(p.getValue()) + 1) + "");
            }
        });

        nameColumn.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().name));

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
        scoreColumn.setCellValueFactory(q -> new SimpleIntegerProperty(q.getValue().score).asObject());
        table.setItems(currentTop10);
    }
}
