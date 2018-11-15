package Pandemic.View.Components;

import Pandemic.Cards.Card;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class HandComponent extends StackPane{
    private List<? extends Card> cards;
    private int width = 500;
    private int height = 300;
    private double scale = 1.0;
    private int angle = 30;
    private int translation;

    private StackPane content;

    public HandComponent(List<? extends Card> cards){
        this.cards = cards;
        this.getChildren().add(content = new StackPane());
    }

    public void refresh(){
        content.setTranslateY(translation * scale);
        content.getChildren().clear();
        content.setAlignment(Pos.BOTTOM_CENTER);

        List<CardComponent> cardComponents = new ArrayList<>();
        for(Card card : cards) cardComponents.add(card.getDrawer());

        int size = cards.size();
        final int defaultAngle = this.angle + 10 * size / 2;
        double angle = defaultAngle / (size - 1);
        int i = 0;
        for (CardComponent component : cardComponents){
            component.setScale(scale);
            component.refresh();


            final double transformAngle = -defaultAngle / 2 + angle * i;
            final double distance = 55 * scale;
            component.getTransforms().addAll(
                    new Rotate(transformAngle, component.getMinWidth() / 2, component.getMinHeight() * 1.3)
            );
            component.setOnMouseEntered(e -> {
                TranslateTransition transition = new TranslateTransition(Duration.millis(100), component);
                transition.setToX(Math.sin(Math.toRadians(transformAngle)) * distance);
                transition.setToY(Math.cos(Math.toRadians(transformAngle)) * -distance);
                transition.play();
            });
            component.setOnMouseExited(e -> {
                TranslateTransition transition = new TranslateTransition(Duration.millis(100), component);
                transition.setToX(0);
                transition.setToY(0);
                transition.play();
            });
            component.setCursor(Cursor.HAND);
            i++;
        }

        content.getChildren().addAll(cardComponents);
    }

    public void setCards(List<Card> c){
        this.cards = c;
    }

    public void hide(){
        scale = 0.8;
        angle = 17;
        translation = 150;
        refresh();
    }

    public void show(){
        scale = 1;
        angle = 30;
        translation = 0;
        refresh();
    }

    public void setScale(double scale){
        this.scale = scale;
    }
}
