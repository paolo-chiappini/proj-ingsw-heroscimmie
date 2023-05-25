package it.polimi.ingsw.client.view.gui.controllers.boardview;

import it.polimi.ingsw.client.view.gui.Animations;
import it.polimi.ingsw.client.view.gui.EventHandlers;
import it.polimi.ingsw.client.view.gui.GuiController;
import it.polimi.ingsw.client.view.gui.SceneManager;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class BoardController extends GuiController {
    public StackPane window;
    public GridPane gamePlane;
    public AnchorPane anchorPane;
    public ImageView backgroundImage;
    public GridPane gridPaneBoard;
    public  Button confirmButton;
    public Button undoButton;
    public Button goToBookshelfButton;
    public Button goToBoardButton;
    public HBox columnsForegroundBox;
    public HBox columnsBox;
    public HBox selectedTilesList;
    public Label selectedTilesListLabel;
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

    private VBox selectedColumn;
    private BoardViewState boardViewState;
    private TilesBoard board;
    private Bookshelf bookshelf;
    private Pane bookshelvesView;
    private BookshelvesViewController bookshelvesViewController;
    private String myName;

    public void startStage(Stage stage){
        foregroundGridPane.setPickOnBounds(false);
//        backgroundImage.fitWidthProperty().bind(stage.widthProperty().multiply(2));
        backgroundImage.fitHeightProperty().bind(stage.heightProperty());

        anchorPane.scaleXProperty().bind(stage.heightProperty().divide(750));
        anchorPane.scaleYProperty().bind(stage.heightProperty().divide(750));

        this.board = new TilesBoard(this, gridPaneBoard);
        this.bookshelf = new Bookshelf(this, columnsBox, columnsForegroundBox);

        EventHandlers.set(this);
//        board.randomFillWith(getFiles());

        boardViewState = new PickUpTilesState(this);

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

    public void playSwitchToBookshelfAnimation(int direction){
        if(gamePlane.getTranslateX()*direction > 0) return;

        setDisabledInterface(true);

        var transition = Animations.getSwitchToBookshelfAnimation(gamePlane, backgroundImage, direction);

        transition.setOnFinished(e-> setDisabledInterface(false));
        transition.play();
    }

    public void setDisabledInterface(boolean b) {
        var gameElements = new ArrayList<>(gamePlane.getChildren());
        gameElements.addAll(foregroundGridPane.getChildren());

        for(var n : gameElements){
            if(n.disableProperty().isBound()) continue;

            if(n == selectedTilesBox){
                selectedTilesList.setDisable(b);
                continue;
            }

            n.setDisable(b);
        }
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

    public void updateBookshelf(int[][] bookshelf, String playerName){
        if (playerName.equals(myName)){
            this.bookshelf.update(bookshelf);
            return;
        }

        bookshelvesViewController.addBookshelf(bookshelf, playerName);
    }

    public void addPlayer(String username, int score, boolean isClient) {
        if(isClient)
            this.myName = username;
    }
//    public void clickCommonGoal(MouseEvent e){
//        var imageView = (ImageView)e.getSource();
//        var url = imageView.getImage().getUrl();
//        var fileName = url.substring( url.lastIndexOf('/')+1);
//        int imageIndex = Integer.parseInt(fileName.substring(0, fileName.lastIndexOf('.')));
//
//        imageIndex = (imageIndex+1) % 12 + 1;
//        String newImage = url.replace(fileName, String.format("%d.jpg", imageIndex));
//        imageView.setImage(new Image(newImage));
//    }
//
//    public void clickScoringToken(MouseEvent e){
//        var imageView = (ImageView)e.getSource();
//        var url = imageView.getImage().getUrl();
//        var fileName = url.substring( url.lastIndexOf('/')+1);
//        int imageIndex = Integer.parseInt(fileName.substring(0, fileName.lastIndexOf('.')));
//
//        imageIndex -= 2;
//        if(imageIndex == 0) imageIndex = 8;
//
//        String newImage = url.replace(fileName, String.format("%d.jpg", imageIndex));
//        imageView.setImage(new Image(newImage));

//    }


//    public List<File> getFiles(){
//        var itemTilesDirectory = SceneManager.class.getResource("/sprites/item_tiles_small");
//        if (itemTilesDirectory == null)
//            throw new RuntimeException("Something went wrong when locating /sprites/publisher_material/box_no_shadow.png");
//
//        File tileImagesDirectory = new File(itemTilesDirectory.getPath()
//                .replace("/", "\\")
//                .replace("%20", " "));
//
//        var tilesFiles = tileImagesDirectory.listFiles();
//        if(!tileImagesDirectory.isDirectory() || tilesFiles == null)
//            throw new RuntimeException("Something went wrong when loading item tiles sprites");
//
//        return List.of(tilesFiles);
//    }
}