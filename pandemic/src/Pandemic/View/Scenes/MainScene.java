package Pandemic.View.Scenes;

import Pandemic.View.PandemicScene;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import Pandemic.Core.Pandemic;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class MainScene extends PandemicScene {
    @FXML private ToggleGroup applicationType;
    @FXML private ToggleGroup gameMode;

    @FXML private RadioButton consoleApplication;
    @FXML private RadioButton graphicsApplication;
    @FXML private RadioButton newButton;
    @FXML private RadioButton loadButton;
    @FXML private Slider difficultySlider;
    @FXML private Pane titleHolder;


    @FXML private MenuButton numOfPlayers;

    private Pandemic game;
    private int difficulty = 3;

    private int numberOfPlayers = 2;

    public MainScene(Pandemic game){
        super("MainScene.fxml");
        this.game = game;
    }

    public void initialize(){
        consoleApplication.setUserData(ApplicationTypes.CONSOLE);
        graphicsApplication.setUserData(ApplicationTypes.GRAPHICS);
        newButton.setUserData(GameModes.NEW);
        loadButton.setUserData(GameModes.LOAD);
    }

    @FXML private void setUpGame(){
        if(applicationType.getSelectedToggle().getUserData() == ApplicationTypes.CONSOLE){
            if(gameMode.getSelectedToggle().getUserData() == GameModes.NEW)
                game.startConsoleApplication(numberOfPlayers, difficulty);
            else if(gameMode.getSelectedToggle().getUserData() == GameModes.LOAD)
                game.loadConsoleApplication();
        }
        else if(applicationType.getSelectedToggle().getUserData() == ApplicationTypes.GRAPHICS)
                if(gameMode.getSelectedToggle().getUserData() == GameModes.NEW)
                    game.startGraphicsApplication(numberOfPlayers, difficulty);
                else if(gameMode.getSelectedToggle().getUserData() == GameModes.LOAD)
                    game.loadGraphicsApplication();
    }

    @FXML private void numOfPlayersChangedTo2(){
        numberOfPlayers = 2;
        numOfPlayers.setText("2 Players");
    }
    @FXML private void numOfPlayersChangedTo3(){
        numberOfPlayers = 3;
        numOfPlayers.setText("3 Players");
    }
    @FXML private void numOfPlayersChangedTo4(){
        numberOfPlayers = 4;
        numOfPlayers.setText("4 Players");
    }

    @FXML private void setDifficulty(){
        difficulty = (int) difficultySlider.getValue();
    }


    private enum GameModes{
        NEW, LOAD;
    }

    private enum ApplicationTypes{
        CONSOLE, GRAPHICS;
    }
}
