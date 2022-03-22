package client.scenes.leaderboards;

import client.scenes.MainCtrl;
import client.utils.AvatarSupplier;
import client.utils.ServerUtils;
import com.talanlabs.avatargenerator.Avatar;
import com.talanlabs.avatargenerator.eightbit.EightBitAvatar;
import commons.LeaderboardEntry;

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

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LeaderboardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private ObservableList<LeaderboardEntry> top10LeaderboardEntries;
    private Avatar builder;

    @FXML
    private TableView<LeaderboardEntry> table;
    @FXML
    private TableColumn rank;
    @FXML
    private TableColumn<LeaderboardEntry, String> nameColumn;
    @FXML
    private TableColumn<LeaderboardEntry, String> avatarColumn;
    @FXML
    private TableColumn<LeaderboardEntry, Integer> scoreColumn;

    @Inject
    public LeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void back(){
        mainCtrl.showGameModeSelection();
    }

    public void load() {
        top10LeaderboardEntries = FXCollections.observableList(server.getTop10Scores());
        builder = EightBitAvatar.newMaleAvatarBuilder().build();

        rank.setCellValueFactory(
                (Callback<TableColumn.CellDataFeatures<LeaderboardEntry, String>, ObservableValue<String>>) q
                -> new SimpleStringProperty((table.getItems().indexOf(q.getValue()) + 1) + ""));

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
            TableCell<LeaderboardEntry, String> cell = new TableCell<LeaderboardEntry, String>() {
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
        table.setItems(top10LeaderboardEntries);
    }
}
