package Pandemic.View.Components;

import Pandemic.Core.Pandemic;
import Pandemic.Players.Player;
import Pandemic.View.Effect;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;


public class ControllerComponent extends BorderPane implements Refreshable {

    Player currentPlayer;

    public ControllerComponent(){
        this.init();
    }

    private void init(){
        HBox main = new HBox();
        main.setBackground(new Background(new BackgroundFill(Effect.gradient(
                Color.color(0.2, 0.2, 0.2),
                Color.color(0.3, 0.3, 0.3),
                3,
                true,
                false
        ), null, null)));
        this.setMinHeight(130);
        main.setMinHeight(130);
        main.setAlignment(Pos.CENTER);
        main.setSpacing(20);

        ControllButton button2 = new ControllButton("Treat", new Image("file:res/treat.png"));
        ControllButton button3 = new ControllButton("Share", new Image("file:res/share.png"));
        ControllButton button4 = new ControllButton("Build", new Image("file:res/build.png"));
        ControllButton button5 = new ControllButton("Antidote", new Image("file:res/antidote.png"));
        ControllButton button6 = new ControllButton("Pass", new Image("file:res/pass.png"));
        ControllButton button7 = new ControllButton("Restart", new Image("file:res/restart.png") );

        main.getChildren().addAll(button7, button2, button3, button4, button5, button6);


        AnchorPane additionalInformaition = new AnchorPane();
        additionalInformaition.setMinHeight(30);
        additionalInformaition.setMaxHeight(30);
        additionalInformaition.setBackground(new Background(new BackgroundFill(Color.color(0.3, 0.3, 0.3), null, null)));
        additionalInformaition.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.grayRgb(1, 0.5), 10, 0.4, 0, 0));

        StackPane mainMessage = new StackPane();
        Effect.setSize(mainMessage, 600, 60);
        mainMessage.setBackground(new Background(new BackgroundFill(Color.color(0.2, 0.2, 0.2), new CornerRadii(70, 70, 0, 0, false), null)));
        mainMessage.setTranslateY(10);
        mainMessage.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.grayRgb(1, 0.5), 20, 0.4, 0, 0));

        StackPane topPart = new StackPane(additionalInformaition, mainMessage);
        topPart.setAlignment(Pos.BOTTOM_CENTER);

        Effect.grow(additionalInformaition, false, true, true, true);

        this.setTop(topPart);
        this.setCenter(main);
    }

    public void setCurrentPlayer(Player p){
        this.currentPlayer = p;
    }
    @Override
    public void refresh() {
        init();
    }

    private class ControllButton extends StackPane {
        private String text;
        private ImageView image;
        ControllButton(String text, Image img){
            this.text = text;
            image = new ImageView();
            image.setImage(img);
            image.setFitHeight(30);
            image.setFitWidth(30);

            this.image = image;
            init();
        }
        private void init(){
            VBox main = new VBox();
            this.setAlignment(Pos.CENTER);
            main.setAlignment(Pos.CENTER);
            main.setSpacing(10);
            Effect.setSize(main, 100, 100);
            main.setBackground(new Background(new BackgroundFill(Effect.gradient(
                    Color.color(1, 1, 1, 0.05), Color.color(1, 1, 1, 0.2), 2, true, false
            ), new CornerRadii(20), null)));
            main.setCursor(Cursor.HAND);

            Text label = new Text(text);
            label.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 17));
            label.setFill(Color.WHITE);
            main.getChildren().addAll(image, label);
            this.getChildren().add(main);

            main.setBorder(new Border(new BorderStroke(Color.color(1, 1, 1, 0.1), BorderStrokeStyle.SOLID, new CornerRadii(20), BorderWidths.DEFAULT)));

            main.setOnMouseEntered(e -> {
                main.setBackground(new Background(new BackgroundFill(Effect.gradient(
                        Color.color(1, 1, 1, 0.2), Color.color(1, 1, 1, 0.05), 2, true, false
                ), new CornerRadii(20), null)));

                label.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.color(1, 1, 1, 0.3), 5, 0.4, 0, 0));
                image.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.color(1, 1, 1, 0.3), 5, 0.4, 0, 0));
            });
            main.setOnMouseExited(e -> {
                main.setBackground(new Background(new BackgroundFill(Effect.gradient(
                        Color.color(1, 1, 1, 0.05), Color.color(1, 1, 1, 0.2), 2, true, false
                ), new CornerRadii(20), null)));
                label.setEffect(null);
                image.setEffect(null);
            });
        }
    }
}
