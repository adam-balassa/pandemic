package Pandemic.View.Scenes;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.util.Duration;

public class Effect {
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

    public static void fadeOut(Parent component){
        FadeTransition transition = new FadeTransition(Duration.millis(200), component);
        TranslateTransition transition2 = new TranslateTransition(Duration.millis(200), component);

        transition.setFromValue(1.0);
        transition.setToValue(0.0);
        transition.setOnFinished( e -> { component.setVisible(false); });
        transition2.setInterpolator(Interpolator.EASE_IN);

        transition2.setFromY(0);
        transition2.setToY(-30);
        transition2.setInterpolator(Interpolator.EASE_IN);

        transition.play();
        transition2.play();
    }
}
