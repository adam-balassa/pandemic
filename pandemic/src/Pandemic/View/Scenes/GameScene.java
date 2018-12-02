package Pandemic.View.Scenes;
import Pandemic.Cards.Card;
import Pandemic.Cards.CityCard;
import Pandemic.Core.Game;
import Pandemic.Core.Hand;
import Pandemic.Core.IGame;
import Pandemic.Core.Virus;
import Pandemic.Exceptions.EndOfGame;
import Pandemic.Exceptions.PandemicException;
import Pandemic.Players.GraphicsPlayer;
import Pandemic.Players.Player;
import Pandemic.Table.Field;
import Pandemic.View.Components.*;
import Pandemic.View.Effect;
import Pandemic.View.PandemicScene;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.*;

public class GameScene extends PandemicScene{
    private IGame game;

    private boolean hidden = false;
    private PlayerComponent activePlayerComponent;
    private List<PlayerComponent> playerComponents;
    private GraphicsPlayer activePlayer;
    private int width = 1250;
    private int height = 680;
    private ControllerComponent controller;
    private BorderPane main;
    private TableComponent table;
    private Pane hideButton;
    private AlertBoxComponent alertBox;
    private HandComponent hand;

    public GameScene(Game game) {
        this.game = game;
    }

    public void init(){
        root = new StackPane();
        ((StackPane)root).setAlignment(Pos.CENTER);
        main = new BorderPane();
        root.setBackground(new Background(new BackgroundImage(new Image("file:res/table.png"),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, new BackgroundSize(1, 1, true, true, false, true))));

        Effect.grow(main);

        main.setPrefHeight(height);
        main.setMinWidth(width);

        controller = new ControllerComponent();
        setControlButtonActions();

        Map<String, Field> fields = game.getFields();
        table = new TableComponent(fields.values());

        //table.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));

        root.getChildren().addAll(main, alertBox = new AlertBoxComponent());

        scene = new Scene(root);
        scene.setCamera(new PerspectiveCamera());
    }

    public static Color colorOfVirus(Virus v){
        switch (v) {
            case RED:
                return Color.color(0.8, 0.1, 0.1, 0.8);
            case YELLOW:
                return Color.color(0.9, 0.7, 0, 0.8);
            case BLACK:
                return Color.color(0, 0, 0, 0.8);
            case BLUE:
                return Color.color(0.1, 0.3, 0.7, 0.8);
        }
        return null;
    }

    public void alert(PandemicException e, String t){
        alertBox.alert(e, t);
    }

    public void message(String msg){
        controller.message(msg);
    }

    public void newRound(GraphicsPlayer player){
        playerComponents = new ArrayList<>();
        Player[] players = game.getPlayers();
        for(Player p: players) playerComponents.add(new PlayerComponent((GraphicsPlayer)p));

        this.activePlayer = player;
        for(PlayerComponent p: playerComponents) {
            if (p.getPlayer() == player) {
                activePlayerComponent = p;
            }
        }
        controller.refresh(game.getBreakOuts(), game.getInfectionStatus());
        controller.setPlayer(activePlayer);

        Pane button = drawButton();
        Pane center = drawPlayers();
        center.getChildren().add(0, drawTable());
        center.getChildren().add(button);
        main.setCenter(center);
        main.setBottom(null);
        main.setBottom(controller);
        this.refresh();
        this.show();
    }

    public void refresh(){
        controller.refresh(activePlayer.getRemainingActions(), game.getAntidotes());
        table.refresh(activePlayer);
        for(PlayerComponent pc: playerComponents) pc.refresh(activePlayer);
    }

    public void restart(){
        Map<String, Field> fields = game.getFields();
        table = new TableComponent(fields.values());
        this.newRound(activePlayer);
    }

    public void endGame(){
        Stage window = (Stage) this.scene.getWindow();
        alert(new EndOfGame("Game over"), "Game over");
        alertBox.getButton().setOnAction(e -> {
            window.close();
        });
    }

    public void hide(){
        Text t = (Text) hideButton.getChildren().get(1);
        int length = 250;
        RotateTransition rt = new RotateTransition(Duration.millis(length), t);
        TranslateTransition tt = new TranslateTransition(Duration.millis(length), t);

        TranslateTransition tt2 = new TranslateTransition(Duration.millis(length), controller);
        TranslateTransition tt3 = new TranslateTransition(Duration.millis(length), activePlayerComponent);
        TranslateTransition tt4 = new TranslateTransition(Duration.millis(length), hideButton);
        SequentialTransition st = new SequentialTransition(new ParallelTransition(tt2, tt3, rt, tt, tt4));

        rt.setToAngle(180);
        tt.setToY(3);
        tt2.setToY(controller.getMinHeight());
        tt3.setToY(controller.getMinHeight());
        tt4.setToY(controller.getMinHeight());
        st.setOnFinished(a -> {
            main.setBottom(null);
            this.activePlayerComponent.hide();
            PlayerComponent.disable(true);
            this.activePlayerComponent.setTranslateY(0);
            hideButton.setTranslateY(0);
        });

        st.play();
    }

    public void show(){
        Text t = (Text) hideButton.getChildren().get(1);
        RotateTransition rt = new RotateTransition(Duration.millis(200), t);
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), t);

        TranslateTransition tt2 = new TranslateTransition(Duration.millis(200), controller);
        TranslateTransition tt3 = new TranslateTransition(Duration.millis(200), activePlayerComponent);
        TranslateTransition tt4 = new TranslateTransition(Duration.millis(200), hideButton);
        SequentialTransition st = new SequentialTransition(new ParallelTransition(tt2, tt3, rt, tt, tt4));

        activePlayerComponent.show();
        PlayerComponent.disable(false);
        rt.setToAngle(0);
        tt.setToY(-3);
        tt2.setToY(0);
        tt3.setFromY(controller.getMinHeight());
        tt3.setToY(0);
        tt4.setFromY(controller.getMinHeight());
        tt4.setToY(0);
        main.setBottom(controller);

        st.play();
    }

    private AnchorPane drawPlayers(){
        AnchorPane center = new AnchorPane();
        Effect.grow(center);

        int aPC = playerComponents.indexOf(activePlayerComponent);
        int numOfPlayers = playerComponents.size();
        int j = aPC;
        List<PlayerComponent> pcs = new ArrayList<>(numOfPlayers);

        do{
            pcs.add(playerComponents.get(j));
            if(++j == numOfPlayers) j = 0;
        } while(j != aPC);
        playerComponents = pcs;

        for(int i = 0; i < numOfPlayers; i++){
            PlayerComponent pc = playerComponents.get(i);
            pc.setTranslateY(0);
            Effect.grow(pc, i == 2, i != 1, i == 0, i != 3);

            pc.hide();
            pc.setRotate(90 * i);
            pc.setTranslateY((i == 1 || i == 3) ? height / 2 - 220 : 0);
            pc.setTranslateX((i == 1 || i == 3) ? (i % 4 - 2) * -40 : 0);
        }

        int[] order = {2, 0, 1, 3};
        for(int i = 0; i < order.length; i++)
            if(numOfPlayers > order[i]) center.getChildren().add(playerComponents.get(order[i]));

        return center;
    }

    private StackPane drawTable(){
        StackPane boardHolder = new StackPane(table);
        boardHolder.setAlignment(Pos.CENTER);
        Effect.grow(boardHolder);
        return boardHolder;
    }

    private Pane drawButton(){
        StackPane buttonHolder = new StackPane();
        buttonHolder.setAlignment(Pos.TOP_LEFT);
        hideButton = getButton();

        Effect.grow(buttonHolder, false, false, true, true);
        buttonHolder.setTranslateX(150);
        buttonHolder.setTranslateY(-20);

        hideButton.setOnMouseClicked(e -> {

            if(!hidden) this.hide();
            else this.show();

            hidden = !hidden;
        });

        buttonHolder.getChildren().add(hideButton);
        return buttonHolder;
    }

    private Pane getButton(){
        Text downArrow = new Text("v");
        downArrow.setScaleX(2);
        downArrow.setFont(Font.font(20));
        downArrow.setFill(Color.color(0.5, 0.5, 0.5, 1));
        downArrow.setTranslateY(-3);

        Pane background = new Pane();
        background.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(25), null)));
        background.setOpacity(0);

        StackPane pane = new StackPane(background);
        pane.getChildren().add(downArrow);
        Effect.setSize(pane, 50, 50);
        pane.setBorder(new Border(new BorderStroke(Color.color(0.5, 0.5, 0.5, 1), BorderStrokeStyle.SOLID, new CornerRadii(25), new BorderWidths(2))));
        pane.setAlignment(Pos.CENTER);

        pane.setCursor(Cursor.HAND);

        pane.setOnMouseEntered(e -> {
            FadeTransition transition = new FadeTransition(Duration.millis(150), pane.getChildren().get(0));
            transition.setToValue(0);
            transition.setToValue(1);
            transition.play();
        });

        pane.setOnMouseExited(e -> {
            FadeTransition transition = new FadeTransition(Duration.millis(150), pane.getChildren().get(0));
            transition.setToValue(1);
            transition.setToValue(0);
            transition.play();
        });
        return pane;
    }

    private void setControlButtonActions(){
        Map<String, ControllerComponent.ControllButton> buttons = controller.getButtons();
        buttons.get("infection").setOnMouseClicked(e -> {
            activePlayer.action(
                    GraphicsPlayer.Interaction.INFECTIONTRASHBUTTONCLICK,
                    new GraphicsPlayer.InteractionOptions.Builder().build());
        });

        buttons.get("main").setOnMouseClicked(e -> {
            activePlayer.action(
                    GraphicsPlayer.Interaction.MAINTRASHBUTTONCLICK,
                    new GraphicsPlayer.InteractionOptions.Builder().build());
        });
    }

    public void openInfectionTrash(List<? extends Card> cards, GraphicsPlayer.Interaction interaction){
        if(cards.size() != 0){
            hand = new HandComponent(cards);
            hand.show();
            root.getChildren().add(hand);
            Effect.fadeIn(hand);
            for(CardComponent c : hand.getCards())
                c.setOnMouseClicked(e -> {
                    activePlayer.action(
                        interaction,
                        new GraphicsPlayer.InteractionOptions.Builder().setCard(c.getCard()).build()); });
            hand.getBackside().setOnMouseClicked(f -> {
                Transition t = Effect.fadeOut(hand);
                t.setOnFinished(e -> {
                    removeHand();
                });
                t.play();
                activePlayer.action(GraphicsPlayer.Interaction.OUTCLICK, null);
            });

        }
    }

    public void removeHand(){
        this.root.getChildren().remove(hand);
    }

    public HandComponent getHand(){
        return hand;
    }

    public void draw(List<Card> drawn, List<Card> infected){
        final HandComponent drawnCards = new HandComponent(drawn);
        final HandComponent infectedCards = new HandComponent(infected);
        drawnCards.show();
        infectedCards.show();

        root.getChildren().add(drawnCards);
        Transition draw1 = Effect.drawCard(drawnCards, scene);
        Transition wait1 = new PauseTransition(Duration.millis(2800));
        FadeTransition fade1 = new FadeTransition(Duration.millis(200), drawnCards);
        fade1.setFromValue(1); fade1.setToValue(2);
        fade1.setOnFinished(e -> {
            root.getChildren().remove(drawnCards);
            root.getChildren().add(infectedCards);
        });

        Transition draw2 = Effect.drawCard(infectedCards, scene);
        Transition wait2 = new PauseTransition(Duration.millis(2800));
        FadeTransition fade2 = new FadeTransition(Duration.millis(200), infectedCards);
        fade2.setFromValue(1); fade2.setToValue(2);
        fade2.setOnFinished(e -> { root.getChildren().remove(infectedCards); });

        SequentialTransition drawing = new SequentialTransition(new PauseTransition(Duration.millis(1000)), draw1, wait1, fade1, draw2, wait2, fade2);
        drawing.play();
    }
}
