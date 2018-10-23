package Pandemic.Core;

import Pandemic.Core.Game;
import Pandemic.Core.Saver;
import Pandemic.Players.ConsolePlayer;
import Pandemic.Players.Player;

public class Application {
    public static void main(String[] args){
        Game game = new Game();

        game.setSaver(new Saver(game, "pandemic.restore.txt"));
        int numOfPlayers = 2;
        Player[] players = new Player[numOfPlayers];
        players[0] = new ConsolePlayer(game, System.in, System.out);
        players[1] = new ConsolePlayer(game, System.in, System.out);

        game.start(players, 3);
    }
}
