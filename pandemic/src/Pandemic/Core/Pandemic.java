package Pandemic.Core;
import Pandemic.Exceptions.EndOfGame;
import Pandemic.Players.ConsolePlayer;
import Pandemic.Players.GraphicsPlayer;
import Pandemic.Players.Player;
import Pandemic.View.Scenes.GameScene;
import Pandemic.View.Scenes.MainScene;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;


public class Pandemic extends Application implements Saver {

    private Stage window;
    private Game game;
    private Player[] players;
    private final File file = new File("pandemic.restore.txt");

    public Pandemic(){
    }

    public static void main(String[] args){
        launch();
        new Pandemic();
    }

    /**
     * JavaFX Application entry
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;
        window.setTitle("Pandemic");
        window.setScene(new MainScene(this).getScene());
        window.show();
    }

    public void startConsoleApplication(int numOfPlayers, int difficulty){
        window.close();
        game = new Game(this);
        players = new Player[numOfPlayers];
        for(int i = 0; i < players.length; i++)
            players[i] = new ConsolePlayer(game, System.in, System.out);
        game.setPlayers(players);
        game.initialize(difficulty);
        game.start();
    }

    /**
     * Starts a new game with graphics interface
     * @param numOfPlayers
     * @param difficulty
     */
    public void startGraphicsApplication(int numOfPlayers, int difficulty){
        window.close();
        game = new Game(this);
        players = new Player[numOfPlayers];
        GameScene scene = new GameScene(game);
        for(int i = 0; i < players.length; i++)
            players[i] = new GraphicsPlayer(game, scene);
        game.setPlayers(players);
        game.initialize(difficulty);
        scene.init();
        window.setScene(scene.getScene());
        window.show();
        try{ game.nextRound(); }catch(EndOfGame e){}
    }

    /**
     * Loads a previously started game with a graphics interface
     */
    public void loadGraphicsApplication(){
        window.close();
        this.game = new Game(this);
        GameScene scene = new GameScene(game);

        Game previousStatus = load();
        players = new Player[previousStatus.getNumberOfPlayers()];

        for(int i = 0; i < players.length; i++)
            players[i] = new GraphicsPlayer(game, scene);
        game.setPlayers(players);
        game.reconstruct(previousStatus);
        scene.init();
        window.setScene(scene.getScene());
        window.show();
        try{ game.nextRound(); }catch(EndOfGame e){}
    }

    public void loadConsoleApplication(){
        window.close();
        this.game = new Game(this);
        Game previousStatus = load();
        players = new Player[previousStatus.getNumberOfPlayers()];
        for(int i = 0; i < players.length; i++)
            players[i] = new ConsolePlayer(game, System.in, System.out);

        game.setPlayers(players);
        game.reconstruct(previousStatus);
        game.start();
    }


    /**
     * Saves the game status into a file
     */
    @Override
    public void save() {
        try {
            new FileWriter(this.file).close();
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
            os.writeObject(game);
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a game status from a file
     * @return
     */
    @Override
    public Game load() {
        ObjectInputStream is;
        try {
            is = new ObjectInputStream(new FileInputStream(file));
            Game game = (Game)is.readObject();
            is.close();
            return game;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
