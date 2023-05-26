package it.polimi.ingsw.client.view.gui;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
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

    public static Transition getItsYourTurnAnimation(Label yourTurnLabel) {
        ParallelTransition transition = new ParallelTransition();
        TranslateTransition descendTransition = new TranslateTransition(Duration.millis(700), yourTurnLabel);
        descendTransition.setInterpolator(Interpolator.EASE_OUT);
        descendTransition.setByY(150);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(700), yourTurnLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(800), yourTurnLabel);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        transition.getChildren().add(fadeIn);
        transition.getChildren().add(descendTransition);

        return new SequentialTransition (
                transition,
                new PauseTransition(Duration.seconds(2)),
                fadeOut
        );

    }
}
