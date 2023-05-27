package it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements;

import it.polimi.ingsw.client.view.gui.Animations;
import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardController;
import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardViewState;
import javafx.animation.*;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;

public class TileElement extends GraphicElement{
    private final ImageView image;
    private final HBox styleBox;
    private int gridPositionX;
    private int gridPositionY;

    public int getGridPositionX() {
        return gridPositionX;
    }

    public int getGridPositionY() {
        return gridPositionY;
    }

    private static final HashMap<HBox, TileElement> tileElementsFromHBox = new HashMap<>();

    public TileElement(BoardController controller, ImageView image, int gridPositionX, int gridPositionY) {
        this(controller, image);
        this.gridPositionX = gridPositionX;
        this.gridPositionY = gridPositionY;
    }

    public TileElement(BoardController controller, ImageView image) {
        super(controller);
        this.image = image;
        this.styleBox = new HBox(image);

        var boardGrid = controller.getBoard();
        image.setPreserveRatio(true);
        image.fitWidthProperty().bind(boardGrid.getWidthProperty().divide(9).multiply(0.9));

        styleBox.setMaxWidth(image.getFitWidth());
        styleBox.setMaxHeight(image.getFitHeight());
        styleBox.getStyleClass().add("tile-box");

        GridPane.setHalignment(styleBox, HPos.CENTER);
        GridPane.setValignment(styleBox, VPos.CENTER);

        styleBox.setOnMouseReleased(this::pickUpTile);
        tileElementsFromHBox.put(styleBox, this);
    }

    public ImageView getImageView(){
        return image;
    }

    private void pickUpTile(MouseEvent e){
        HBox tile = (HBox)e.getSource();
        BoardViewState state = controller.getState();
        state.clickTile(tile);
    }

    @Override
    public Node getElementAsNode() {
        return styleBox;
    }

    public void playDropAnimation(VBox selectedVBoxColumn) {

        var MILLIS_PER_PIXEL = 0.8;
        var FALL_DELAY = 150;

        var numberOfTilesInColumn = selectedVBoxColumn.getChildren().size();

        TranslateTransition fallAnimation = new TranslateTransition();
        fallAnimation.setInterpolator(Interpolator.EASE_IN);

        var colSpacing = selectedVBoxColumn.getSpacing();
        var imgHeight = image.getFitHeight();
        var colHeight = (imgHeight+colSpacing)*6-colSpacing;
        double margin = (imgHeight+colSpacing)*2;


        var startY = colHeight-(imgHeight+colSpacing)*numberOfTilesInColumn + margin;
        fallAnimation.setDuration(Duration.millis(MILLIS_PER_PIXEL*startY));

        fallAnimation.setNode(image);
        fallAnimation.setFromY(startY);
        fallAnimation.setToY(0);
        SequentialTransition seqTransition = new SequentialTransition (
                new PauseTransition(Duration.millis(FALL_DELAY)), // wait a second
                fallAnimation
        );
        controller.setDisabledInterface(true);
        seqTransition.setOnFinished(e-> controller.setDisabledInterface(false));
        seqTransition.play();
    }

    public void playInvalidSelectionAnimation(){
        int ANGLE = 25;
        double DURATION = 100;
        int CYCLES = 2;
        double SCALING = 1.2;

        ParallelTransition parallelTransition = new ParallelTransition();

        ScaleTransition scaleAnimation = new ScaleTransition();
        scaleAnimation.setNode(styleBox);
        scaleAnimation.setDelay(Duration.millis(DURATION*CYCLES+DURATION/2));
        scaleAnimation.setFromX(SCALING);
        scaleAnimation.setFromY(SCALING);
        scaleAnimation.setToX(1);
        scaleAnimation.setToY(1);
        scaleAnimation.setDuration(Duration.millis(50));

        var animation = Animations.getErrorAnimation(styleBox,  ANGLE, DURATION, CYCLES);

        parallelTransition.getChildren().add(animation);
        parallelTransition.getChildren().add(scaleAnimation);
        parallelTransition.play();
    }

    public static TileElement getInstanceFromNode(Node n){
        return tileElementsFromHBox.get((HBox) n);
    }
}
