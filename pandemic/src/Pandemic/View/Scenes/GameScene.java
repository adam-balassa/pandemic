package Pandemic.View.Scenes;

import Pandemic.Cards.CityCard;
import Pandemic.Core.Game;
import Pandemic.Core.IGame;
import Pandemic.Core.Pandemic;
import Pandemic.Core.Virus;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Exceptions.PandemicException;
import Pandemic.Table.Field;
import Pandemic.View.Components.*;
import Pandemic.View.Effect;
import Pandemic.View.PandemicScene;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.Cursor;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Map;
import java.util.Stack;

public class GameScene extends PandemicScene {
    private IGame game;

    @FXML private StackPane alertBox;
    @FXML private TitledPane alertBoxTitle;
    @FXML private Label alertBoxText;
    @FXML private Label alertBoxNote;
    @FXML private Button alertBoxButton;

    private boolean hidden = false;
    private PlayerComponent activePlayer;
    private int width = 1100;
    private int height = 680;
    private ControllerComponent controller;
    private BorderPane main;
    private Pane hideButton;

    public GameScene(Game game) {
        this.game = game;
        this.init();

        scene = new Scene(root);
        scene.setCamera(new PerspectiveCamera());
    }

    private void init(){
        root = new StackPane();
        main = new BorderPane();

        Effect.grow(main);

        main.setPrefHeight(height);
        main.setMinWidth(width);

        controller = new ControllerComponent();
        controller.setCurrentPlayer(game.getCurrentPlayer());


        AnchorPane center = new AnchorPane();
        Effect.grow(center);

        Map<String, Field> fields = game.getFields();
        TableComponent board = new TableComponent(fields.values());
        StackPane boardHolder = new StackPane(board);
        boardHolder.setAlignment(Pos.CENTER);
        Effect.grow(boardHolder);


        activePlayer = new PlayerComponent(game.getCurrentPlayer());
        Effect.grow(activePlayer, false, true, true, true);
        center.getChildren().addAll(activePlayer);

        PlayerComponent player3 = new PlayerComponent(game.getPlayers()[3]);
        player3.hide();
        player3.setRotate(180);
        Effect.grow(player3, true, true, false, true);
        center.getChildren().add(player3);

        PlayerComponent player = new PlayerComponent(game.getPlayers()[1]);
        player.hide();
        player.setRotate(90);
        Effect.grow(player, false, false, false, true);
        center.getChildren().add(player);

        PlayerComponent player2 = new PlayerComponent(game.getPlayers()[2]);
        player2.hide();
        player2.setRotate(-90);
        Effect.grow(player2, false, true, false, false);
        center.getChildren().add(player2);

        addButton(center);
        center.getChildren().add(0, boardHolder);
        main.setCenter(center);
        main.setBottom(controller);

        root.getChildren().addAll(main);
    }

    private void alert(PandemicException e, String title){
        alertBoxTitle.setText(title);
        alertBoxText.setText(e.getMessage());
        alertBoxNote.setText(e.getHelp());
        Effect.fadeIn(alertBox);
    }

    @FXML private void alertBoxClosed(){
        Effect.fadeOut(alertBox);
    }

    @FXML private void alertB(){
        alert(new CannotPerformAction("You must drop a card"), "Drop a card");
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

    private void addButton(Pane pane){
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
        pane.getChildren().add(buttonHolder);
    }

    private void hide(){
        Text t = (Text) hideButton.getChildren().get(1);
        RotateTransition rt = new RotateTransition(Duration.millis(200), t);
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), t);

        TranslateTransition tt2 = new TranslateTransition(Duration.millis(200), controller);
        TranslateTransition tt3 = new TranslateTransition(Duration.millis(200), activePlayer);
        TranslateTransition tt4 = new TranslateTransition(Duration.millis(200), hideButton);
        SequentialTransition st = new SequentialTransition(new ParallelTransition(tt2, tt3, rt, tt, tt4));

        rt.setToAngle(180);
        tt.setToY(3);
        tt2.setToY(controller.getMinHeight());
        tt3.setToY(controller.getMinHeight());
        tt4.setToY(controller.getMinHeight());
        st.setOnFinished(a -> {
            main.setBottom(null);
            activePlayer.hide();
            activePlayer.setMouseTransparent(true);
            activePlayer.setTranslateY(0);
            hideButton.setTranslateY(0);
        });

        st.play();
    }

    private void show(){
        Text t = (Text) hideButton.getChildren().get(1);
        RotateTransition rt = new RotateTransition(Duration.millis(200), t);
        TranslateTransition tt = new TranslateTransition(Duration.millis(200), t);

        TranslateTransition tt2 = new TranslateTransition(Duration.millis(200), controller);
        TranslateTransition tt3 = new TranslateTransition(Duration.millis(200), activePlayer);
        TranslateTransition tt4 = new TranslateTransition(Duration.millis(200), hideButton);
        SequentialTransition st = new SequentialTransition(new ParallelTransition(tt2, tt3, rt, tt, tt4));

        activePlayer.show();
        activePlayer.setMouseTransparent(false);
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
}
