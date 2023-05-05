package it.polimi.ingsw.client.view.cli.graphics.simple;

import java.util.LinkedList;

/**
 * Represents a generic rectangular element.
 */
public class RectangleElement extends CliElement {
    public RectangleElement(int width, int height) {
        this.representation = new LinkedList<>();
        for (int i = 0; i < height; i++) {
            this.representation.add(new LinkedList<>());
            for (int j = 0; j < width; j++) this.representation.get(i).add(null);
        }
    }
}
