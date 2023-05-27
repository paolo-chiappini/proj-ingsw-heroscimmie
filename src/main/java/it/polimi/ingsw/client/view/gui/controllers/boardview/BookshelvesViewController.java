package it.polimi.ingsw.client.view.gui.controllers.boardview;

import it.polimi.ingsw.client.view.gui.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class BookshelvesViewController {
    @FXML
    private FlowPane bookshelvesPane;
    private final HashMap<String, Pane> bookshelves;

    public BookshelvesViewController() {
        this.bookshelves = new HashMap<>();
    }

    /**
     * Adds a player's bookshelf to the GUI. If it already exists, it updates it.
     * @param bookshelf representation of the bookshelf
     * @param playerName name of the player
     */
    public void addBookshelf(int[][] bookshelf, String playerName){
        if(bookshelves.containsKey(playerName)){
            updateBookshelf(bookshelf, playerName);
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/fxmls/mini_bookshelf.fxml"));
        try {
            Pane newBookshelf = fxmlLoader.load();
            Pane bookshelfElement = (Pane) newBookshelf.getChildren().get(0);
            Label playerNameLabel = (Label) newBookshelf.getChildren().get(1);

            playerNameLabel.setText(playerName);
            GridPane grid = (GridPane) bookshelfElement.getChildren().get(0);

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    Rectangle rec = new Rectangle();

                    setTileFill(rec, bookshelf[i][j]);

                    rec.heightProperty().bind(
                            grid.heightProperty().add(grid.getVgap())
                                    .divide(grid.getRowCount())
                                    .subtract(grid.getVgap()));

                    rec.widthProperty().bind(rec.heightProperty());
                    grid.add(rec, j, i);
                }
            }

            bookshelves.put(playerName, newBookshelf);
            bookshelvesPane.getChildren().add(newBookshelf);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBookshelf(int[][] bookshelf, String playername) {
        Pane bookShelf = bookshelves.get(playername);

        Pane bookshelfElement = (Pane) bookShelf.getChildren().get(0);
        GridPane grid = (GridPane) bookshelfElement.getChildren().get(0);

        for (int k = 0; k < grid.getChildren().size(); k++) {
            int i = k/5;
            int j = k%5;

            Rectangle rec = (Rectangle) grid.getChildren().get(k);
            setTileFill(rec, bookshelf[i][j]);
        }
    }

    private void setTileFill(Rectangle rec, int colorIndex) {
        Color colorFill = null;
        switch (colorIndex){
            case -1 -> colorFill = Color.TRANSPARENT;
            case 0  -> colorFill = Color.rgb(144, 164, 65); //CAT
            case 1  -> colorFill = Color.rgb(236, 225, 190); //BOOK
            case 2  -> colorFill = Color.rgb(96, 180, 177); //TROPHY
            case 3  -> colorFill = Color.rgb(197, 76, 124); //PLANT
            case 4  -> colorFill = Color.rgb(0, 100, 140); //FRAME
            case 5  -> colorFill = Color.rgb(220, 164, 60); //TOYT
        }

        rec.setFill(colorFill);
    }

}