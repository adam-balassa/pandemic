package Pandemic.Core;
import Pandemic.Players.ConsolePlayer;
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

    public void startGraphicsApplication(int numOfPlayers, int difficulty){
        window.setScene(new GameScene(this).getScene());
        window.show();
    }

    public void loadGraphicsApplication(){

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

    @Override
    public Game load() {
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(new FileInputStream(file));
            Game game = (Game)is.readObject();
            is.close();
            return game;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("zgkugvkgb\n\n\n");
        return null;
    }
}
