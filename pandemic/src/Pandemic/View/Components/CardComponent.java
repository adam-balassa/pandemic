package Pandemic.View.Components;

import javafx.animation.TranslateTransition;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

abstract public class CardComponent extends BorderPane {
    public abstract Pane init();
    public abstract void setScale(double s);
}
