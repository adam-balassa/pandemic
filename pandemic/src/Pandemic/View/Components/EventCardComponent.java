package Pandemic.View.Components;

import Pandemic.Cards.EventCard;
import Pandemic.View.Scenes.GameScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class EventCardComponent extends CardComponent {
    private EventCard card;

    public EventCardComponent(EventCard e){
        card = e;
    }

    @Override
    protected void init() {
        super.init();
        this.setBackground(new Background(new BackgroundFill(Color.color(0, 0, 0, 0.5), radius, null)));

        BorderPane center = new BorderPane();
        center.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, radius, BorderWidths.DEFAULT)));
        center.setBackground(new Background(new BackgroundFill(Color.color(0.9, 0.65, 0), radius, null)));
        Label label = new Label(card.getName().toUpperCase());
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20 * scale));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setTextFill(Color.color(0, 0, 0, 0.8));
        label.setWrapText(true);

        StackPane textBox = new StackPane(label);;
        textBox.setPadding(new Insets(5 * scale, 20 * scale, 5 * scale,  20 * scale));
        textBox.setBackground(new Background(new BackgroundFill(
                Color.WHITE,
                new CornerRadii(radius.getTopLeftHorizontalRadius(), radius.getTopRightHorizontalRadius(), 0d, 0d, false),
                Insets.EMPTY)));
        textBox.setMinHeight(60);
        textBox.setAlignment(Pos.CENTER);

        Label population = new Label("Event");
        population.setFont(Font.font("Arial", FontWeight.BOLD, 13 * scale));
        population.setTextFill(Color.WHITESMOKE);
        VBox populationBox = new VBox(population);
        populationBox.setAlignment(Pos.CENTER_RIGHT);
        populationBox.setBackground(new Background(new BackgroundFill(Color.color(0, 0, 0, 0.8), null, null)));
        population.setPadding(new Insets(5 * scale, 10 * scale, 5 * scale, 10 * scale));

        center.setTop(new VBox(textBox, populationBox));

        Label description = new Label(card.getDescription());
        description.setFont(Font.font("Arial", FontWeight.NORMAL, 16 * scale));
        description.setTextAlignment(TextAlignment.CENTER);
        description.setTextFill(Color.color(0.8, 0.2, 0, 1));
        description.setWrapText(true);
        description.setPadding(new Insets(10));

        center.setCenter(description);


        this.setCenter(center);
    }
}
