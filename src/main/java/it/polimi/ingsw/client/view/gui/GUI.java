package it.polimi.ingsw.client.view.gui;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        List<String> p = getParameters().getRaw();
        if(p.isEmpty()){
            SceneManager.splashScreenScene(stage);
        }
        else{
            switch (p.get(0)){
                case "--menu" -> SceneManager.menuScene(stage);
                case "--board" -> SceneManager.mainGameScene(stage);
            }
        }
    }
    public static void main(String[] args) {launch(args);}

}