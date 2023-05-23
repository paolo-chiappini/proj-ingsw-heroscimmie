package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.gui.controllers.MenuController;
import it.polimi.ingsw.client.view.gui.controllers.MenuJoinGameController;
import it.polimi.ingsw.client.view.gui.controllers.MenuNewGameController;
import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardController;

public abstract class EventHandlers {
    public static void set(MenuController controller){
        controller.newGameButton.setOnMouseReleased(e-> {
            SceneManager.newGameScene(controller, controller.innerStackPane);
        });

        controller.joinGameButton.setOnMouseReleased(e->{
            SceneManager.joinGameScene(controller, controller.innerStackPane);
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

    public static void set(MenuJoinGameController controller) {
        controller.confirmButton.requestFocus();
        controller.confirmButton.disableProperty().bind(controller.nameTextField.textProperty().isEmpty());
        controller.confirmButton.setOnMouseClicked(controller::setUsername);
    }

    public static void set(MenuNewGameController controller) {
        controller.twoPlayerButton.requestFocus();
        controller.twoPlayerButton.disableProperty().bind(controller.nameTextField.textProperty().isEmpty());
        controller.threePlayerButton.disableProperty().bind(controller.nameTextField.textProperty().isEmpty());
        controller.fourPlayerButton.disableProperty().bind(controller.nameTextField.textProperty().isEmpty());

        controller.twoPlayerButton.setOnMouseClicked(controller::setUsername);
        controller.threePlayerButton.setOnMouseClicked(controller::setUsername);
        controller.fourPlayerButton.setOnMouseClicked(controller::setUsername);
    }
}
