package Pandemic.View;

import Pandemic.View.Components.CardComponent;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.*;
import javafx.scene.transform.Shear;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Effect {
    private static boolean transitionPlaying;
    public static void fadeIn(Parent component){
        FadeTransition transition = new FadeTransition(Duration.millis(200), component);
        TranslateTransition transition2 = new TranslateTransition(Duration.millis(200), component);

        transition.setFromValue(0);
        transition.setToValue(1.0);
        transition.setInterpolator(Interpolator.EASE_IN);

        transition2.setFromY(-30);
        transition2.setToY(0);
        transition2.setInterpolator(Interpolator.EASE_IN);

        component.setVisible(true);
        transition2.play();
        transition.play();
    }

    public static Transition fadeOut(Parent component){
        FadeTransition transition = new FadeTransition(Duration.millis(200), component);
        TranslateTransition transition2 = new TranslateTransition(Duration.millis(200), component);
        ParallelTransition pt = new ParallelTransition(transition, transition2);

        transition.setFromValue(1.0);
        transition.setToValue(0.0);
        transition2.setInterpolator(Interpolator.EASE_IN);

        transition2.setFromY(0);
        transition2.setToY(-30);
        transition2.setInterpolator(Interpolator.EASE_IN);

        pt.setOnFinished( e -> { component.setVisible(false); });
        return pt;
    }

    public static Transition drawCard(Node card, Scene scene){
        RotateTransition flipping = new RotateTransition(Duration.millis(700), card);
        flipping.setAxis(new Point3D(-100,  0, 0));
        flipping.setFromAngle(-90);
        flipping.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double t) {
                return t * t;
            }
        });
        flipping.setToAngle(0);


        TranslateTransition moving = new TranslateTransition(Duration.millis(700), card);
        Bounds position = card.localToScene(card.getLayoutBounds());
        moving.setFromY(scene.getHeight() / 2 - (position.getMinY() + position.getMaxY()) / 2 - scene.getHeight() / 2);
        moving.setToY(- scene.getHeight() / 2);
        moving.setFromZ(200);
        moving.setToZ(0);
        moving.setInterpolator(new Interpolator() {
            @Override
            protected double curve(double t) {
                return t * t;
            }
        });
        return new ParallelTransition(flipping, moving);
    }

    public static Paint gradient(Color color1, Color color2, int numOfStops, boolean linear, boolean dir){
        List<Stop> stops = new ArrayList<>();
        for(int i = 0; i < numOfStops; i++){
            stops.add(new Stop(((double)i / (numOfStops - 1)), (i % 2 == 0) ? color1 : color2));
        }
        if(linear){
            return new LinearGradient(0, 0, dir ? 1 : 0, dir ? 0 : 1, true, CycleMethod.NO_CYCLE, stops);
        }
        else {
            return new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, stops);
        }
    }

    public static void grow(Node component){
        AnchorPane.setRightAnchor(component, 0d);
        AnchorPane.setLeftAnchor(component, 0d);
        AnchorPane.setBottomAnchor(component, 0d);
        AnchorPane.setTopAnchor(component, 0d);
    }

    public static void resetGrow(Node component){
        AnchorPane.setRightAnchor(component, null);
        AnchorPane.setLeftAnchor(component, null);
        AnchorPane.setBottomAnchor(component, null);
        AnchorPane.setTopAnchor(component, null);
    }

    public static void grow(Node component, boolean top, boolean right, boolean bottom, boolean left){
        AnchorPane.setRightAnchor(component, right ? 0d : null);
        AnchorPane.setLeftAnchor(component, left ? 0d : null);
        AnchorPane.setBottomAnchor(component, bottom ? 0d : null);
        AnchorPane.setTopAnchor(component, top ? 0d : null);
    }

    public static void setSize(Pane component, double width, double height){
        component.setMinWidth(width);
        component.setMinHeight(height);
        component.setMaxWidth(width);
        component.setMaxHeight(height);
    }
}
