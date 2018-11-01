package Pandemic.View.Scenes;

import Pandemic.Core.Pandemic;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Exceptions.PandemicException;
import Pandemic.View.PandemicScene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.StackPane;

public class GameScene extends PandemicScene {
    Pandemic game;

    private Object lock = new Object();

    @FXML private StackPane alertBox;
    @FXML private TitledPane alertBoxTitle;
    @FXML private Label alertBoxText;
    @FXML private Label alertBoxNote;
    @FXML private Button alertBoxButton;

    public GameScene(Pandemic game) {
        super("GameScene.fxml");
        this.game = game;
    }

    private void alert(PandemicException e, String title){
        alertBoxTitle.setText(title);
        alertBoxText.setText(e.getMessage());
        alertBoxNote.setText(e.getHelp());
        Effect.fadeIn(alertBox);
        //lock.wait();
        //alertBox.setVisible(false);
    }

    @FXML private void alertBoxClosed(){
        Effect.fadeOut(alertBox);
    }

    @FXML private void alertB(){
        alert(new CannotPerformAction("You must drop a card"), "Drop a card");
    }
}
