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
    private final String[] names;
    @FXML
    private FlowPane bookshelvesPane;
    private final HashMap<String, Pane> bookshelves;

    public BookshelvesViewController() {
        this.bookshelves = new HashMap<>();
        this.names = new String[]{"Giorgio", "Gianni", "Giuseppa"};
    }

    public void setup(){
        for(int k = 0; k<3; k++){
            Random r = new Random();

            int[][] randomArray = new int[6][5];
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    var randomInt = r.nextInt(12)-6;
                    randomArray[i][j] = (randomInt+Math.abs(randomInt))/2;
                }
            }
            addBookshelf(randomArray, names[k]);
        }

        bookshelvesPane.setOnMouseClicked(e->{
            for(int k = 0; k<3; k++){
                Random r = new Random();

                int[][] randomArray = new int[6][5];
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        var randomInt = r.nextInt(12)-6;
                        randomArray[i][j] = (randomInt+Math.abs(randomInt))/2;
                    }
                }
                updateBookshelf(randomArray, names[k]);
            }
        });
    }

    public void addBookshelf(int[][] bookshelf, String playername){
        if(bookshelves.containsKey(playername)){
            updateBookshelf(bookshelf, playername);
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource("/fxmls/mini_bookshelf.fxml"));
        try {
            Pane newBookshelf = fxmlLoader.load();
            Pane bookshelfElement = (Pane) newBookshelf.getChildren().get(0);
            Label playerName = (Label) newBookshelf.getChildren().get(1);

            playerName.setText(playername);
            GridPane grid = (GridPane) bookshelfElement.getChildren().get(0);

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 5; j++) {
                    Rectangle rec = new Rectangle();
                    var colorFill = Color.hsb((double) bookshelf[i][j] /5*255, 0.8, 0.8);

                    if(bookshelf[i][j] == 0)
                        colorFill = Color.TRANSPARENT;

                    rec.setFill(colorFill);
                    rec.heightProperty().bind(
                            grid.heightProperty().add(grid.getVgap())
                                    .divide(grid.getRowCount())
                                    .subtract(grid.getVgap()));

                    rec.widthProperty().bind(rec.heightProperty());
                    grid.add(rec, j, i);
                }
            }

            bookshelves.put(playername, newBookshelf);
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

            var colorFill = Color.hsb((double) bookshelf[i][j] /5*255, 0.8, 0.8);

            if(bookshelf[i][j] == 0)
                colorFill = Color.TRANSPARENT;

            Rectangle rec = (Rectangle) grid.getChildren().get(k);

            rec.setFill(colorFill);
        }
    }

}