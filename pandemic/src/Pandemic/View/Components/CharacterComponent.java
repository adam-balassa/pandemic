package Pandemic.View.Components;

import Pandemic.View.Effect;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

public class CharacterComponent extends StackPane {
    private Color color;
    private double size = 25;
    public CharacterComponent(Color color){
        this.color = color;
        Effect.setSize(this, size, size * 1.3);
        this.setAlignment(Pos.TOP_CENTER);
        init();
    }
    private void init(){
        double x = size / 2;
        Circle circle = new Circle(size / 2, size / 2, size / 2);
        circle.setFill(color);
        Polygon triangle = new Polygon(
                0.2*x, x *1.2,
                2*x - 0.2*x, x * 1.2,
                x, 2*x);
        triangle.setTranslateY(1.65 * x);
        triangle.setFill(color);
        this.getChildren().addAll(circle, triangle);
    }
}
