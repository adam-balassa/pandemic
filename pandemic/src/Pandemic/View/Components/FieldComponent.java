package Pandemic.View.Components;

import Pandemic.Characters.Character;
import Pandemic.Core.Virus;
import Pandemic.Players.GraphicsPlayer;
import Pandemic.Table.Field;
import Pandemic.View.Effect;
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

public class FieldComponent extends StackPane {
    private Field field;

    private int width = 40;
    private int height = 55;
    private double scale = 1;
    private Color color;
    private List<CharacterComponent> characterComponents;
    private GraphicsPlayer currentPlayer;

    public FieldComponent(Field f){
        this.field = f;
        this.color = GameScene.colorOfVirus(f.getColor());
        characterComponents = new ArrayList<>();
        Effect.setSize(this, width * scale, height* scale);
    }

    public void refresh(GraphicsPlayer newCurrentPlayer) {
        this.currentPlayer = newCurrentPlayer;
        this.getChildren().clear();
        characterComponents.clear();
        Effect.setSize(this, width * scale, height* scale);
        this.setAlignment(Pos.TOP_CENTER);

        StackPane center = new StackPane();
        Circle centerCircle = new Circle(width * scale / 2, width * scale / 2, width * scale * 0.3, Color.WHITESMOKE);
        centerCircle.setEffect(new DropShadow(BlurType.GAUSSIAN, color, 20 * scale , 0.1, 0, 0));
        centerCircle.setStroke(color);
        centerCircle.setStrokeWidth(1);
        Stop[] stops = new Stop[]{
                new Stop(0,  Color.color(0.9, 0.9, 0.9, 0.8)),
                new Stop(1, color)
        };
        if(!this.field.hasStation()){
            centerCircle.setFill(new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE, stops));
        }

        for(Character c: field.getCharacters()){
            CharacterComponent character = new CharacterComponent(c.getColor());
            character.setOnMouseClicked(e -> {
                currentPlayer.action(
                    GraphicsPlayer.Interaction.CHARACTERCLICK,
                    new GraphicsPlayer.InteractionOptions.Builder().setCharacter(c).build()
                );
            });
            characterComponents.add(character);
        }

        int numOfCharacters = characterComponents.size();
        double distance = 8;
        for(int i = 0; i < numOfCharacters; i++){
            CharacterComponent character = characterComponents.get(i);
            character.setTranslateX(distance * (i - (numOfCharacters - 1.0) / 2.0));
            character.setTranslateY(-1);
            character.setCursor(Cursor.HAND);
        }

        StackPane virusPane = new StackPane();
        center.setCursor(Cursor.HAND);
        center.setOnMouseEntered(e -> {
            RotateTransition transition = new RotateTransition(Duration.millis(200), virusPane);
            transition.setByAngle(180);
            transition.play();
        });
        center.setOnMouseExited(e -> {
            RotateTransition transition = new RotateTransition(Duration.millis(200), virusPane);
            transition.setByAngle(-180);
            transition.play();
        });
        centerCircle.setOnMouseClicked(e -> {
            currentPlayer.action(
                    GraphicsPlayer.Interaction.FIELDCLICK,
                    new GraphicsPlayer.InteractionOptions.Builder().setField(this.field).build()
            );
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
        this.getChildren().addAll(characterComponents);
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
