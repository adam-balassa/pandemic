package Pandemic.View.Components;

import Pandemic.Cards.Card;
import Pandemic.View.Effect;
import Pandemic.View.Scenes.GameScene;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

abstract public class CardComponent extends BorderPane {
    protected CornerRadii radius = new CornerRadii(10);
    protected int width = 200;
    protected int height = 300;
    protected double scale = 1;
    protected Color color;

    protected void init(){
        this.setPadding(new Insets(20 * scale, 10 * scale, 20 * scale, 10 * scale));
        this.setEffect(new DropShadow(BlurType.GAUSSIAN, Color.color(0, 0, 0, 0.4), 10, 0.3, 2, 2));
        this.setBorder(new Border(new BorderStroke(Color.color(0, 0, 0, 0.1), BorderStrokeStyle.SOLID, radius, BorderWidths.DEFAULT)));

        Effect.setSize(this, width*scale, height*scale);
    }

    public void refresh(){
        init();
    }

    public void setScale(double scale){
        this.scale = scale;
        radius = new CornerRadii(10 * scale);
    }

    abstract public Card getCard();
}
