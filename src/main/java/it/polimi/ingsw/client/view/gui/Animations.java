package it.polimi.ingsw.client.view.gui;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class Animations {
    public static Transition getErrorAnimation(Node node, int ANGLE, double DURATION, int CYCLES){

        SequentialTransition animation = new SequentialTransition();

        RotateTransition mainRotation = new RotateTransition();
        mainRotation.setNode(node);
        mainRotation.setFromAngle(-ANGLE);
        mainRotation.setToAngle(ANGLE);
        mainRotation.setDuration(Duration.millis(DURATION));
        mainRotation.setCycleCount(CYCLES);
        mainRotation.setAutoReverse(true);

        RotateTransition returnRotation = new RotateTransition();
        returnRotation.setNode(node);
        returnRotation.setFromAngle(-ANGLE);
        returnRotation.setToAngle(0);
        returnRotation.setDuration(Duration.millis(DURATION));

        animation.getChildren().add(mainRotation);
        animation.getChildren().add(returnRotation);
        return animation;
    }

    public static Transition getSwitchToBookshelfAnimation(Pane table, Node background , int direction){
        ParallelTransition transition = new ParallelTransition();
        TranslateTransition tableTransition = new TranslateTransition();
        tableTransition.setNode(table);
        tableTransition.setDuration(Duration.millis(300));
        tableTransition.setInterpolator(Interpolator.EASE_OUT);
        var distance = table.getWidth()/2;
        tableTransition.setByX( direction * distance);

        TranslateTransition backgroundTransition = new TranslateTransition();
        backgroundTransition.setNode(background);
        backgroundTransition.setDuration(Duration.millis(300));
        backgroundTransition.setInterpolator(Interpolator.EASE_OUT);
        backgroundTransition.setByX( direction * distance*0.4);

        transition.getChildren().add(tableTransition);
        transition.getChildren().add(backgroundTransition);
        return transition;
    }

}
