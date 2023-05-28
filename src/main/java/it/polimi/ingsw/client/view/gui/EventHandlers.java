package it.polimi.ingsw.client.view.gui;

import it.polimi.ingsw.client.view.gui.controllers.*;
import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardController;
import javafx.beans.binding.Bindings;
public abstract class EventHandlers {
    public static void set(MenuController controller){
        controller.newGameButton.setOnMouseReleased(e-> SceneManager.newGameScene(controller, controller.innerStackPane));
        controller.joinGameButton.setOnMouseReleased(e-> SceneManager.joinGameScene(controller, controller.innerStackPane));
        controller.loadGameButton.setOnMouseReleased(e-> SceneManager.loadGameScene(controller, controller.innerStackPane));
    }
    public static void set(BoardController controller){
        controller.toggleBookshelvesButton.setOnMouseClicked(controller::toggleBookshelvesView);

        controller.chatButton.setOnMouseReleased(controller::openChat);
        controller.saveGameButton.setOnMouseReleased(controller::saveGame);

        controller.confirmButton.setOnMouseClicked(e-> controller.getState().clickConfirmButton());

        controller.undoButton.setOnMouseClicked(e-> controller.getState().clickUndoButton());

        controller.goToBoardButton.setOnMouseReleased(e-> controller.playSwitchToBookshelfAnimation(1));

        controller.goToBookshelfButton.setOnMouseReleased(e-> controller.playSwitchToBookshelfAnimation(-1));
    }

    public static void set(SubMenuController controller){
        controller.undo.setOnMouseClicked(controller::returnToMenu);
    }

    public static void set(MenuLoadGameController controller){
        controller.confirmButton.requestFocus();
        var selectedIndeces = controller.gamesList.getSelectionModel().getSelectedIndices();

        controller.confirmButton.disableProperty().bind(
                controller.nameTextField.textProperty().isEmpty()
                        .or(Bindings.size(selectedIndeces).greaterThan(1))
                        .or(Bindings.size(selectedIndeces).isEqualTo(0)));

        controller.confirmButton.setOnMouseClicked(controller::setUsername);
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
