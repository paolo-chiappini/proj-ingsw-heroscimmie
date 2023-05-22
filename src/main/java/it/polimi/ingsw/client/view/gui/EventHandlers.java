package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.gui.controllers.MenuController;
import it.polimi.ingsw.client.view.gui.controllers.MenuNewGameController;
import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardController;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public abstract class EventHandlers {
    public static void set(MenuController controller){
        controller.newGameButton.setOnMouseReleased(e-> SceneManager.newGameScene(controller, controller.innerStackPane));

        controller.joinGameButton.setOnMouseReleased(e->{
            Alert alert = new Alert(Alert.AlertType.NONE,
                    "Mi spiace ma non ho ancora implementato sta roba",
                    ButtonType.OK);
            alert.setTitle("Scusa bro");
            alert.showAndWait();
        });
    }
    public static void set(BoardController controller){
        controller.commonGoalCardBottom.setOnMouseClicked(controller::clickCommonGoal);
        controller.commonGoalCardTop.setOnMouseClicked(controller::clickCommonGoal);
        controller.toggleBookshelvesButton.setOnMouseClicked(controller::toggleBookshelvesView);
        controller.scoringTokenTop.setOnMouseClicked(controller::clickScoringToken);
        controller.scoringTokenBottom.setOnMouseClicked(controller::clickScoringToken);

        controller.confirmButton.setOnMouseClicked(e-> controller.getState().clickConfirmButton());

        controller.undoButton.setOnMouseClicked(e-> controller.getState().clickUndoButton());

        controller.goToBoardButton.setOnMouseReleased(e-> controller.playSwitchToBookshelfAnimation(1));

        controller.goToBookshelfButton.setOnMouseReleased(e-> controller.playSwitchToBookshelfAnimation(-1));
    }

    public static void set(MenuNewGameController controller) {
        controller.backButton.setOnMouseReleased(e->{
            controller.root.getChildren().clear();
            controller.root.getChildren().addAll(controller.lastView);
        });
        controller.confirmButton.requestFocus();
        controller.confirmButton.disableProperty().bind(controller.nameTextField.textProperty().isEmpty());
        controller.confirmButton.setOnMouseClicked(controller::setUsername);
    }
}