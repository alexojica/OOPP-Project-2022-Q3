/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {


        var home = FXML.load(HomeCtrl.class, "client", "scenes", "Home.fxml");
        var gameModeSelection = FXML.load(GameModeSelectionCtrl.class, "client", "scenes", "GameModeSelection.fxml");
        var leaderboard = FXML.load(LeaderboardCtrl.class, "client", "scenes", "Leaderboard.fxml");
        var waiting = FXML.load(WaitingCtrl.class, "client", "scenes", "Waiting.fxml");
        var gameMCQ = FXML.load(GameMCQCtrl.class, "client", "scenes", "GameMCQ.fxml");
        var estimationQuestion = FXML.load(EstimationQuestionCtrl.class, "client", "scenes", "EstimationQuestion.fxml");
        var tempLeaderboard = FXML.load(TempLeaderboardCtrl.class, "client", "scenes", "TempLeaderboard.fxml");
        var gameOver = FXML.load(GameOverCtrl.class, "client", "scenes", "GameOver.fxml");
        var multiPlayerMenu = FXML.load(MultiplayerMenuCtrl.class, "client", "scenes", "MultiplayerMenu.fxml");
        var usernamePopUp = FXML.load(UsernamePopUpCtrl.class, "client", "scenes", "UsernamePopUp.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, home, leaderboard, gameModeSelection, multiPlayerMenu, estimationQuestion, gameMCQ, gameOver, waiting, tempLeaderboard, usernamePopUp);
    }
}