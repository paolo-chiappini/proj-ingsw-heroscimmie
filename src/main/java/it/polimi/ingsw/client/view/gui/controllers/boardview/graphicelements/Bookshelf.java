package it.polimi.ingsw.client.view.gui.controllers.boardview.graphicelements;

import it.polimi.ingsw.client.view.gui.Animations;
import it.polimi.ingsw.client.view.gui.controllers.boardview.BoardController;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class Bookshelf extends GraphicElement{
    private final HBox columnsBox;

    public ObservableList<Node> getColumns() {
        return columns;
    }

    private final ObservableList<Node> columns;
    private final ObservableList<Node> foregroundColumns;
    private final List<ImageView> droppedTiles = new ArrayList<>();

    public Bookshelf(BoardController controller, HBox columnsBox, HBox foregroundColumnsBox) {
        super(controller);
        this.columnsBox = columnsBox;

        this.columns = columnsBox.getChildren();
        this.foregroundColumns = foregroundColumnsBox.getChildren();

        for(var col : columns){
            col.setOnMousePressed(e-> controller.getState().clickColumn((VBox)col));
        }
    }

    public void clearForeground(){
        for(var col : foregroundColumns){
            col.setStyle("");
        }
    }

    public void selectColumn(VBox column){
        clearForeground();

        var foregroundCol = foregroundColumns.get(columns.indexOf(column));
        foregroundCol.setStyle("""
                -fx-border-style: dashed;
                -fx-border-width: 3px;
                -fx-border-color: #e8ce93;
                """);
        controller.setSelectedColumn(column);
        droppedTiles.clear();
    }

    public void dropTile(TileElement tileElement){
        clearForeground();
        ImageView tileImage = tileElement.getImageView();
        var selectedColumn = controller.getSelectedColumn();

        tileImage.fitWidthProperty().unbind();
        tileImage.fitHeightProperty().unbind();
        tileImage.setRotate(180); //yes, the tiles are upside down
        tileImage.setPreserveRatio(false);
        tileImage.setFitWidth(selectedColumn.getWidth());
        tileImage.setFitHeight(selectedColumn.getWidth());

        selectedColumn.getChildren().add(tileImage);
        droppedTiles.add(tileImage);
        tileElement.playDropAnimation(selectedColumn);
    }

    public List<TileElement> returnTiles(){
        List<TileElement> returnedTiles = new ArrayList<>();
        for (var placedTile : droppedTiles){
            placedTile.setRotate(0);
            returnedTiles.add(new TileElement(controller, placedTile));
        }
        return returnedTiles;
    }

    @Override
    public Node getElementAsNode() {
        return columnsBox;
    }

    public void playInvalidColumnAnimation(VBox column) {
        clearForeground();
        columnsBox.setDisable(true);

        var foregroundCol = foregroundColumns.get(columns.indexOf(column));
        foregroundCol.setStyle("""
                -fx-border-style: dashed;
                -fx-border-width: 3px;
                -fx-border-color: #ff0000;
                """);

        int ANGLE = 5;
        double DURATION = 100;
        int CYCLES = 2;
        var animation = Animations.getErrorAnimation(foregroundCol, ANGLE, DURATION, CYCLES);

        animation.setOnFinished(e->{
            clearForeground();
            columnsBox.setDisable(false);
        });
        animation.play();
    }

    public void update(int[][] bookshelf) {
        for (int j = 0; j < 5; j++){
            VBox column = (VBox)columns.get(j);
            column.getChildren().clear();
            for(int i = 5; i >= 0; i--){
                int tileIndexType = bookshelf[i][j];
                if(tileIndexType == -1) continue;
                var imageUrl = getClass().getResource("/sprites/item_tiles_small/" +tileIndexType+".png");
                var tileImage = new ImageView(new Image(imageUrl.toString()));

                tileImage.setRotate(180);
                tileImage.setPreserveRatio(false);
                tileImage.setFitWidth(column.getWidth());
                tileImage.setFitHeight(column.getWidth());

                column.getChildren().add(tileImage);
            }
        }
    }
}
