package it.polimi.ingsw.client.view.gui.controllers.boardview;

import it.polimi.ingsw.client.view.gui.Animations;
import it.polimi.ingsw.client.view.gui.EventHandlers;
import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.SceneManager;
import it.polimi.ingsw.client.view.gui.controllers.EndgameWindowController;
import it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements.Bookshelf;
import it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements.TilesBoard;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

//This is where the fun begins

public class BoardController extends GuiController {

    //FXMLS Nodes
    public StackPane window;
    public GridPane gamePlane;
    public AnchorPane anchorPane;
    public ImageView backgroundImage;
    public GridPane gridPaneBoard;
    public Button confirmButton;
    public Button undoButton;
    public Button goToBookshelfButton;
    public Button goToBoardButton;
    public HBox columnsForegroundBox;
    public HBox columnsBox;
    public HBox selectedTilesList;
    public Label selectedTilesListLabel;
    public Label yourTurnLabel;
    public Label notYourTurnLabel;
    public ImageView boardImage;
    public StackPane boardStackPane;
    public GridPane foregroundGridPane;
    public Pane selectedTilesBox;
    public ImageView commonGoalCardTop;
    public ImageView commonGoalCardBottom;
    public ImageView scoringTokenTop;
    public ImageView scoringTokenBottom;
    public ImageView personalGoalCard;
    public Button toggleBookshelvesButton;
    public StackPane bookshelfPane;
    public VBox playersList;


    //Internal stuff
    private VBox selectedColumn;
    private BoardViewState boardViewState;
    private TilesBoard board;
    private Bookshelf bookshelf;
    private Pane bookshelvesView;
    private BookshelvesViewController bookshelvesViewController;
    private String myName;

    private final HashMap<Integer, ImageView> getCardFromId = new HashMap<>(); //Used for updating GUI elements
    private final List<Player> players = new ArrayList<>();

    public void startStage(Stage stage) {

        foregroundGridPane.setPickOnBounds(false); //For mouse transparency

        //Stuff for making magic (reactivity) possible
        backgroundImage.fitHeightProperty().bind(stage.heightProperty());
        anchorPane.scaleXProperty().bind(stage.heightProperty().divide(750));
        anchorPane.scaleYProperty().bind(stage.heightProperty().divide(750));

        //Initialize graphic elements
        this.board = new TilesBoard(this, gridPaneBoard);
        this.bookshelf = new Bookshelf(this, columnsBox, columnsForegroundBox);

        this.boardViewState = new PickUpTilesState(this);

        EventHandlers.set(this);

        //Other bookshelves view and controller initialization
        FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/fxmls/bookshelves_view.fxml"));
        try {
            this.bookshelvesView = fxmlLoader.load();
            bookshelvesView.setVisible(false);

            GridPane.setHalignment(bookshelvesView, HPos.CENTER);
            GridPane.setValignment(bookshelvesView, VPos.BOTTOM);
            GridPane.setMargin(bookshelvesView, new Insets(0, 0, 10, 0));
            gamePlane.add(bookshelvesView, 3, 0);

            this.bookshelvesViewController = fxmlLoader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.show();
    }

    public ObservableList<Node> getSelectedTilesList() {
        return selectedTilesList.getChildren();
    }

    public void setState(BoardViewState newState) {
        boardViewState = newState;
    }

    public BoardViewState getState() {
        return boardViewState;
    }

    public void setSelectedColumn(VBox selectedColumn) {
        this.selectedColumn = selectedColumn;
    }

    public VBox getSelectedColumn() {
        return selectedColumn;
    }

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public void playSwitchToBookshelfAnimation(int direction) {
        if ((gamePlane.getTranslateX() == 0 && direction == 1) //Do not play animation if already in position
                || (gamePlane.getTranslateX() != 0 && direction == -1)) {
            return;
        }

        setDisabledInterface(true); //Disable everything when moving

        var transition = Animations.getSwitchToBookshelfAnimation(gamePlane, backgroundImage, direction);

        transition.setOnFinished(e -> setDisabledInterface(false));
        transition.play();
    }

    public TilesBoard getBoard() {
        return board;
    }

    public void updateBoard(int[][] update) {
        board.update(update);
    }

    public void toggleBookshelvesView(MouseEvent ignoredE) {
        bookshelfPane.setVisible(!bookshelfPane.isVisible());
        bookshelvesView.setVisible(!bookshelvesView.isVisible());
    }

    public void updateBookshelf(int[][] bookshelf, String playerName) {
        if (playerName.equals(myName)) {
            this.bookshelf.update(bookshelf); //If this is my bookshelf use the Bookshelf GraphicElement
            return;
        }

        bookshelvesViewController.addBookshelf(bookshelf, playerName);
    }


    public void setCurrentTurn(int turn) {
        var players = playersList.getChildren();
        for(int i = 0; i < players.size(); i++){
            players.get(i).getStyleClass().remove("current-player-name");

            if(i == turn){
                players.get(i).getStyleClass().add("current-player-name");
            }
        }
    }

    //I am not proud of this

    public void setCommonGoalCard(int i, int points) {
        var cardUrl = getClass().getResource("/sprites/common_goal_cards/" + i + ".jpg");
        Image cardImage = new Image(cardUrl.toString());

        if (commonGoalCardTop.getImage() == null) { //Set if not set
            commonGoalCardTop.setImage(cardImage);
            getCardFromId.put(i, commonGoalCardTop);
        } else {
            commonGoalCardBottom.setImage(cardImage);
            getCardFromId.put(i, commonGoalCardBottom);
        }

        var tokenUrl = getClass().getResource("/sprites/scoring_tokens/" + points + ".jpg");

        Image tokenImage = null;
        if (points != 0) {
            tokenImage = new Image(tokenUrl.toString());
        }

        if (scoringTokenTop.getImage() == null) {
            scoringTokenTop.setImage(tokenImage);
        } else {
            scoringTokenBottom.setImage(tokenImage);
        }
    }
    public void setPersonalGoalCard(int id) {
        var imageUrl = getClass().getResource("/sprites/personal_goal_cards/" + id + ".png");
        personalGoalCard.setImage(new Image(imageUrl.toString()));
    }

    public void updatePoints(int cardId, int points) {
        var tokenUrl = getClass().getResource("/sprites/scoring_tokens/" + points + ".jpg");

        Image tokenImage = null;
        if (points != 0) {
            tokenImage = new Image(tokenUrl.toString());
        }

        if (getCardFromId.get(cardId) == commonGoalCardTop) {
            scoringTokenTop.setImage(tokenImage);
        } else {
            scoringTokenBottom.setImage(tokenImage);
        }
    }

    public void blockCommands() {
        setDisableCommands(true);
    }

    public void unblockCommands() {
        setDisableCommands(false);
        playSwitchToBookshelfAnimation(1);
        var animation = Animations.getItsYourTurnAnimation(yourTurnLabel);
        animation.setOnFinished(e -> yourTurnLabel.setVisible(false));

        yourTurnLabel.setVisible(true);
        animation.play();
    }

    public void setDisabledInterface(boolean b) {
        var gameElements = new ArrayList<>(gamePlane.getChildren());
        gameElements.addAll(foregroundGridPane.getChildren());

        for (var n : gameElements) { //Disables everything apart from the tiles-box and label
            if (n.disableProperty().isBound()) continue;

            if (n == selectedTilesBox) {
                selectedTilesList.setDisable(b);
                continue;
            }

            n.setDisable(b);
        }
    }

    public void setDisableCommands(boolean b) {
        boardStackPane.setDisable(b); //Can't touch the board if it's not your turn
        boardStackPane.setEffect( b ? new SepiaTone() : null );
        notYourTurnLabel.setVisible(b);
    }

    public void notifyValidMove() {
        boardViewState.notifyValidMove();
    }

    public void notifyInvalidMove() {
        boardViewState.notifyInvalidMove();
    }

    public void addPlayer(String username, int score, boolean isClient) {
        players.add(new Player(username, score));

        if (isClient)
            this.myName = username;

        var playerLabel = new Label(players.size() +") "+username);
        playerLabel.getStyleClass().add("player-name");
        playersList.getChildren().add(playerLabel);
    }

    public void updatePlayerScore(String player, int score) {
        var updatedPlayer = players.stream()
                .filter(p->p.getName().equals(player)).toList().get(0);
        updatedPlayer.setPoints(score);
    }

    public void endGame(String winner){
        System.out.println("WINNER:");
        setDisabledInterface(true);
        FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/fxmls/end_game_view.fxml"));
        try {
            Pane endgameView = fxmlLoader.load();
            EndgameWindowController endgameWindowController = fxmlLoader.getController();

            players.sort(Comparator.comparingInt(Player::getPoints).reversed());

            endgameWindowController.setWinner(winner, players.get(0).getPoints());

            for(int i = 0; i<players.size(); i++){
                var p = players.get(i);
                if(p.getName().equals(winner)) continue;
                endgameWindowController.setOtherPlayer(p.getName(), p.getPoints(), i+1);
            }

            window.getChildren().add(endgameView);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}