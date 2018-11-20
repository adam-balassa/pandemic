package Pandemic.View.Components;

import Pandemic.Cards.Card;
import Pandemic.Cards.CityCard;
import Pandemic.View.Scenes.GameScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class CityCardComponent extends CardComponent{
    private CityCard card;

    public CityCardComponent(CityCard card){
        super();
        this.card = card;
        this.color = GameScene.colorOfVirus(card.getColor());
    }

    @Override
    protected void init(){
        super.init();
        Stop[] stops = new Stop[]{
                new Stop(0, color),
                new Stop(1,  Color.TRANSPARENT)};

        this.setBackground(new Background(new BackgroundFill(
                new LinearGradient(1, 1, 0, 0, true, CycleMethod.NO_CYCLE, stops),
                radius,
                null)));

        BorderPane center = new BorderPane();
        center.setBorder(new Border(new BorderStroke(GameScene.colorOfVirus(card.getColor()), BorderStrokeStyle.SOLID, radius, BorderWidths.DEFAULT)));
        center.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, radius, null)));
        Label label = new Label(card.getCity().getName().toUpperCase());
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20 * scale));
        label.setTextFill(Color.WHITE);
        label.setWrapText(true);

        VBox textBox = new VBox(label);

        center.setPadding(new Insets(20 * scale, 0, 20 * scale, 0));
        textBox.setPadding(new Insets(5 * scale, 20 * scale, 5 * scale,  20 * scale));

        textBox.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops),
                CornerRadii.EMPTY,
                Insets.EMPTY)));

        Label population = new Label("Population: " + card.getPopulation());
        population.setFont(Font.font("Arial", FontWeight.BOLD, 13 * scale));
        population.setTextFill(Color.WHITESMOKE);
        VBox populationBox = new VBox(population);
        populationBox.setAlignment(Pos.CENTER_RIGHT);
        populationBox.setBackground(new Background(new BackgroundFill(Color.color(0, 0, 0, 0.8), null, null)));
        population.setPadding(new Insets(5 * scale, 10 * scale, 5 * scale, 10 * scale));

        center.setTop(new VBox(textBox, populationBox));
        this.setCenter(center);
    }

    @Override
    public Card getCard() {
        return card;
    }
}

