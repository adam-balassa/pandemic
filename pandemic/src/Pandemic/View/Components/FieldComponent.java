package Pandemic.View.Components;

import Pandemic.Core.Virus;
import Pandemic.Table.Field;
import Pandemic.View.Scenes.GameScene;
import javafx.animation.RotateTransition;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FieldComponent extends StackPane implements Refreshable {
    private Field field;

    private int width = 40;
    private int height = 55;
    private double scale = 1;
    private Color color;

    public FieldComponent(Field f){
        this.field = f;
        this.color = GameScene.colorOfVirus(f.getColor());
        refresh();
    }


    @Override
    public void refresh() {
        this.getChildren().removeAll();
        this.setMaxWidth(width * scale);
        this.setMinWidth(width * scale);
        this.setMinHeight(height * scale);
        this.setMaxHeight(height * scale);
        this.setAlignment(Pos.TOP_CENTER);
        //this.setBorder(new Border(new BorderStroke(Color.color(0, 0, 0, 0.1), BorderStrokeStyle.SOLID, null, BorderWidths.DEFAULT)));

        StackPane center = new StackPane();
        Circle centerCircle = new Circle(width * scale / 2, width * scale / 2, width * scale * 0.3, Color.WHITESMOKE);
        centerCircle.setEffect(new DropShadow(BlurType.GAUSSIAN, color, 20 * scale , 0.1, 0, 0));
        centerCircle.setStroke(color);
        centerCircle.setStrokeWidth(1);
        Stop[] stops = new Stop[]{
                new Stop(0,  Color.WHITESMOKE),
                new Stop(1, color)
        };
        if(!this.field.hasStation()){
            centerCircle.setFill(new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, stops));
        }

        StackPane virusPane = new StackPane();
        this.setCursor(Cursor.HAND);
        this.setOnMouseEntered(e -> {
            RotateTransition transition = new RotateTransition(Duration.millis(200), virusPane);
            transition.setByAngle(180);
            transition.play();
        });
        this.setOnMouseExited(e -> {
            RotateTransition transition = new RotateTransition(Duration.millis(200), virusPane);
            transition.setByAngle(-180);
            transition.play();
        });


        setInfection(virusPane);
        center.getChildren().addAll(virusPane, centerCircle);
        this.getChildren().add(center);

        Text text = new Text(field.getName());
        text.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12 * scale));
        text.setFill(Color.WHITESMOKE);
        text.setEffect(new DropShadow(BlurType.GAUSSIAN, color, 2 * scale , 0.8, 0, 0));
        text.setTextAlignment(TextAlignment.CENTER);
        text.setTranslateY(this.height * scale - text.getFont().getSize());

        this.getChildren().add(text);
        //this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        //this.setBlendMode(BlendMode.ADD);
    }

    private void setInfection(StackPane holder){
        Map<Virus, Integer> infections = field.getInfection();
        List<Rectangle> viruses = new ArrayList<>();

        for (Map.Entry<Virus, Integer> infection : infections.entrySet())
            for(int i = 0; i < infection.getValue(); i++)
                viruses.add(getVirus(infection.getKey()));
        if(viruses.size() == 0) return;

        double angle = 360 / viruses.size();

        int i = 0;
        int start = (int)(Math.random() * 360);
        for (Rectangle r : viruses){
            r.setRotate(start + angle * i);
            double a = Math.toRadians(start + angle * i);
            r.setTranslateX(Math.sin(a) * 15 * scale);
            r.setTranslateY(Math.cos(a) * -15 * scale);
            i++;
        }

        holder.getChildren().addAll(viruses);

    }

    private Rectangle getVirus(Virus v){
        double width = 10 * scale;
        Color color = GameScene.colorOfVirus(v);
        Rectangle rect = new Rectangle(width, width);
        rect.setArcHeight(10 * scale / 4);
        rect.setArcWidth(10 * scale / 4);
        rect.setFill(new Color(color.getRed(), color.getGreen(), color.getBlue(), 0.3));
        rect.setEffect(new DropShadow(BlurType.GAUSSIAN, color, 12 * scale , 0.1, 0, 0));
        rect.setStroke(color);
        rect.setStrokeWidth(1);
        return rect;
    }

    public void setScale(double s){
        this.scale = s;
    }

    public Field getField(){
        return field;
    }
}
