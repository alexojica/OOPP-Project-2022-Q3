package client.scenes.leaderboards;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.LeaderboardEntry;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javax.inject.Inject;

public class LeaderboardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ObservableList<LeaderboardEntry> top10LeaderboardEntries;

    @FXML
    private TableView<LeaderboardEntry> table;
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
        nameColumn.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().name));
        avatarColumn.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().avatarPath));
        scoreColumn.setCellValueFactory(q -> new SimpleIntegerProperty(q.getValue().score).asObject());
        table.setItems(top10LeaderboardEntries);
    }
}
