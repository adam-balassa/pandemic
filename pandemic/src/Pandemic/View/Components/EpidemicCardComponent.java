package Pandemic.View.Components;

import Pandemic.Cards.Card;
import Pandemic.Cards.EpidemicCard;
import Pandemic.Cards.EventCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class EpidemicCardComponent extends CardComponent {

    private final String imagePath = "file:res/cover.jpg";
    public EpidemicCardComponent(){
    }

    @Override
    protected void init() {
        super.init();
        this.setBackground(new Background(new BackgroundFill(Color.color(0, 0, 0, 0.5), radius, null)));

        this.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, radius, BorderWidths.DEFAULT)));

        BorderPane center = new BorderPane();
        center.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, radius, BorderWidths.DEFAULT)));
        center.setBackground(new Background(new BackgroundImage(
                new Image(imagePath),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(2.6, 1, true, true, false, false))));

        Text label = new Text("EPIDEMIC");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 25 * scale));
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFill(Color.color(0.9, 0.75, 0.3, 1));
        label.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.BLACK, 10, 0.4, 2, 2));
        label.setScaleY(1.5);

        StackPane textBox = new StackPane(label);;
        textBox.setPadding(new Insets(5 * scale, 20 * scale, 5 * scale,  20 * scale));
        textBox.setMinHeight(60);
        textBox.setAlignment(Pos.CENTER);

        center.setTop(new VBox(textBox));


        this.setCenter(center);
    }

    @Override
    public Card getCard() {
        return new EpidemicCard();
    }
}
