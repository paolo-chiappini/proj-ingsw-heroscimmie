package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.gui.controllers.*;
import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;

public class SceneManager {
    private static GuiController controller = null;
    public static void nextScene(SplashScreenController controller, Stage rootStage){
        FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/fxmls/menu_view.fxml"));
        try {
            Platform.setImplicitExit(false); //Do not exit application when all stages are closed
            rootStage.close();
            Platform.setImplicitExit(true);

            Scene nextScene = new Scene(fxmlLoader.load());
            var menuController = fxmlLoader.<MenuController>getController();
            Stage newStage = new Stage();

            setStageProperties(newStage, nextScene);
            menuController.startStage(newStage);

            SceneManager.controller = menuController;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void nextScene(GUI start, Stage rootStage) throws IOException {
        var res = SceneManager.class.getResource("/fxmls/splash_view.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(res);
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.rgb(0, 0, 0, 0));

        setStageProperties(rootStage, scene);
        rootStage.initStyle(StageStyle.TRANSPARENT);
        rootStage.show();
        SceneManager.controller = fxmlLoader.getController();
    }

    public static void nextScene(MenuController controller, Stage rootStage) {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/fxmls/board_view.fxml"));

        Platform.setImplicitExit(false); //Do not exit application when all stages are closed
        rootStage.close();
        Platform.setImplicitExit(true);

        Scene nextScene;
        try {
            nextScene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong when loading board_view.fxml\n" + e);
        }
        BoardController boardController = fxmlLoader.getController();
        Stage newStage = new Stage();

        setStageProperties(newStage, nextScene);
        boardController.startStage(newStage);

        SceneManager.controller = fxmlLoader.getController();
    }

    public static void newGameScene(MenuController controller, Pane rootElement){
        var lastView = new ArrayList<>(rootElement.getChildren());
        FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/fxmls/menu_view_new_game.fxml"));
        try {
            var newGameView = fxmlLoader.load();
            MenuNewGameController nextController = fxmlLoader.getController();
            rootElement.getChildren().clear();
            rootElement.getChildren().add((Node)newGameView);
            nextController.start(lastView, rootElement);

            SceneManager.controller = fxmlLoader.getController();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void joinGameScene(MenuController controller, Pane rootElement){
        var lastView = new ArrayList<>(rootElement.getChildren());
        FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/fxmls/menu_view_join_game.fxml"));
        try {
            var newGameView = fxmlLoader.load();
            MenuJoinGameController nextController = fxmlLoader.getController();
            rootElement.getChildren().clear();
            rootElement.getChildren().add((Node)newGameView);
            nextController.start(lastView, rootElement);

            SceneManager.controller = fxmlLoader.getController();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void waitGameScene(GuiController controller, Pane rootElement){
        FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/fxmls/menu_view_wait_game.fxml"));
        try {
            var newGameView = fxmlLoader.load();
            rootElement.getChildren().clear();
            rootElement.getChildren().add((Node)newGameView);

            SceneManager.controller = fxmlLoader.getController();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }



    public static GuiController getCurrentController(){
        return SceneManager.controller;
    }

    private static void setStageProperties(Stage stage, Scene scene){
        stage.setFullScreenExitHint("Press F11 to exit fullscreen");
        stage.setTitle("My Shelfie™");

        stage.setOnCloseRequest(e-> GuiController.getView().shutdown());

        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (KeyCode.F11.equals(event.getCode())) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });

        stage.setScene(scene);

        var iconURL = SceneManager.class.getResource("/sprites/publisher_material/box_no_shadow.png");
        if (iconURL == null)
            throw new RuntimeException("Something went wrong when locating /sprites/publisher_material/box_no_shadow.png");

        var iconPath = iconURL.toExternalForm();
        Image icon = new Image(iconPath);
        stage.getIcons().add(icon);
    }
}
