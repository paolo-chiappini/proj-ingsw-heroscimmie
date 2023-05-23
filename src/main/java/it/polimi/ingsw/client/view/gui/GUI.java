package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.gui.controllers.MenuController;
import it.polimi.ingsw.client.view.gui.controllers.SplashScreenController;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        List<String> p = getParameters().getRaw();
        if(p.isEmpty()){
            SceneManager.nextScene(this, stage);
        }
        else{
            switch (p.get(0)){
                case "--menu" -> SceneManager.nextScene(new SplashScreenController(), stage);
                case "--board" -> SceneManager.nextScene(new MenuController(), stage);
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

}