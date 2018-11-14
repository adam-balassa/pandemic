package Pandemic.View.Components;

import javafx.animation.*;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class PandemicButton extends StackPane{
    private Color glow;
    private Color text;
    private Color backgroundColor1 = Color.color(0.03, 0.2, 0.02, 0.8);
    private Color backgroundColor2 = Color.color(0.2, 0.6, 0.05, 0.8);
    private Color hiddenColor = Color.color(0.2, 0.9, 0.1, 0.6);
    private Font textFont;
    private int shadowSize = 20;
    private int width = 200;
    private int height= 42;
    private int borderRadius = 10;
    private Button button;
    private Rectangle background;

    public PandemicButton(){
        super();
        this.glow = Color.color(0.2, 0.65, 0.1);
        this.text = Color.WHITESMOKE;
        this.textFont = Font.font("Lucida Sans Unicode");
        createButton();
        init();
    }

    public PandemicButton(int width, int height, int shadowSize, int borderRadius,
                          Color glowColor, Color tectColor, Color backgroundColor1, Color backgroundColor2, Color hiddenColor){
                this.width = width; this.height = height; this.shadowSize = shadowSize; this.borderRadius = borderRadius;
                this.glow = glowColor; this.text = tectColor;
                this.backgroundColor1 = backgroundColor1; this.backgroundColor2 = backgroundColor2;
                this.hiddenColor = hiddenColor;

                this.textFont = Font.font("Lucida Sans Unicode");
                createButton();
                init();
            }

    private void createButton(){
        this.button = new Button();
        this.background = new Rectangle(width, height);
        background.setArcHeight(borderRadius * 2);
        background.setArcWidth(borderRadius * 2);
        background.setFill(hiddenColor);


        button.setFont(this.textFont);
        button.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        button.setTextFill(text);
        button.setMinWidth(width);
        button.setMinHeight(height);

        button.setMaxWidth(width);
        button.setMaxHeight(height);
        Stop[] stops = new Stop[]{
                new Stop(0, backgroundColor1),
                new Stop(1,  backgroundColor2)};

        button.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops),
                new CornerRadii(borderRadius),
                Insets.EMPTY)));

        this.hoverOut();
        button.setOnMouseEntered(e -> { hoverOn(); });
        button.setOnMouseExited(e -> { hoverOut(); });
        button.setCursor(Cursor.HAND);
        button.setBlendMode(BlendMode.ADD);
    }

    private void init(){
        this.setMaxHeight(height);
        this.setPrefWidth(width);
        this.getStyleClass().add("pandemic-button");
        this.getChildren().addAll(shadow(),background, button);
        this.setEffect(new Bloom(1));
    }

    private Pane shadow(){
        Pane shadowPane = new Pane();

        shadowPane.setMinWidth(width + 2 * shadowSize);
        shadowPane.setMinHeight(height + 2 * shadowSize);
        shadowPane.setMaxWidth(width + 2 * shadowSize);
        shadowPane.setMaxHeight(height + 2 * shadowSize);

        shadowPane.setEffect(new DropShadow(BlurType.GAUSSIAN, glow, shadowSize, 0.4, 0, 0));
        shadowPane.setStyle("-fx-background-color: white; -fx-background-insets: " + shadowSize + "; -fx-background-radius: " + borderRadius);

        Rectangle innerRect = new Rectangle();
        innerRect.setArcWidth(borderRadius * 2);
        innerRect.setArcHeight(borderRadius  * 2);
        Rectangle outerRect = new Rectangle();
        shadowPane.layoutBoundsProperty().addListener(
                (observable, oldBounds, newBounds) -> {
                    innerRect.relocate(
                            newBounds.getMinX() + shadowSize,
                            newBounds.getMinY() + shadowSize
                    );
                    innerRect.setWidth(newBounds.getWidth() - shadowSize * 2);
                    innerRect.setHeight(newBounds.getHeight() - shadowSize * 2);

                    outerRect.setWidth(newBounds.getWidth());
                    outerRect.setHeight(newBounds.getHeight());

                    Shape clip = Shape.subtract(outerRect, innerRect);
                    shadowPane.setClip(clip);
                }
        );
        shadowPane.setBlendMode(BlendMode.ADD);
        return shadowPane;
    }

    public void setText(String str){
        this.button.setText(str);
    }

    public StringProperty textProperty(){
        return this.button.textProperty();
    }

    public void setOnAction(EventHandler<ActionEvent> value){
        this.button.setOnAction(value);
    }

    private void hoverOn(){
        FadeTransition fade = new FadeTransition(Duration.millis(100), background);
        fade.setFromValue(0);
        fade.setToValue(0.2);
        fade.play();
    }

    private void hoverOut(){
        FadeTransition fade = new FadeTransition(Duration.millis(100), background);
        fade.setFromValue(0.2);
        fade.setToValue(0);
        fade.play();
    }
}
