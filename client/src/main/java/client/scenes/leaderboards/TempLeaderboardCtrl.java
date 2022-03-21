package client.scenes.leaderboards;

import client.data.ClientData;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Player;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import com.google.inject.Inject;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TempLeaderboardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ClientData clientData;
    private ObservableList<Player> currentTop10;

    @FXML
    private TableView<Player> table;
    @FXML
    private TableColumn<Player, String> nameColumn;
    @FXML
    private TableColumn<Player, String> avatarColumn;
    @FXML
    private TableColumn<Player, Integer> scoreColumn;

    @Inject
    public TempLeaderboardCtrl(ServerUtils server, MainCtrl mainCtrl, ClientData clientData) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.clientData = clientData;
    }

    public void leaveGame() {
        mainCtrl.showGameModeSelection();
    }

    public void load() {
        currentTop10 = FXCollections.observableList(server.getTopByLobbyToken(clientData.getClientLobby().getToken()));
        nameColumn.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().name));
        avatarColumn.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().avatar));
        scoreColumn.setCellValueFactory(q -> new SimpleIntegerProperty(q.getValue().score).asObject());
        table.setItems(currentTop10);
    }
}
