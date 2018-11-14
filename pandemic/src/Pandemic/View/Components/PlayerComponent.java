package Pandemic.View.Components;

import Pandemic.Cards.Card;
import Pandemic.Players.Player;
import Pandemic.View.Effect;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.util.Duration;

import java.util.List;

public class PlayerComponent extends HandComponent {

    private Player player;
    private boolean hidden = false;
    HandComponent hand;

    public PlayerComponent(Player p) {
        super(p.getCards());
        this.setMaxWidth(0);
        /*this.setAlignment(Pos.CENTER);

        hand = new HandComponent(p.getCards());
        this.getChildren().add(hand);
        hand.setBackground(new Background(new BackgroundFill(Color.RED, null, null)));*/
    }

    /*public void hide(){
        hand.hide();
    }

    public void show(){
        hand.show();
    }*/

    public Player getPlayer(){
        return player;
    }
}
