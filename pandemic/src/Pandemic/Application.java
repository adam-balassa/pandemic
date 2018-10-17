package Pandemic;

import Pandemic.Cards.CityCard;
import Pandemic.Characters.Character;
import Pandemic.Characters.OperationExpert;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Players.ConsolePlayer;
import Pandemic.Players.Player;
import Pandemic.Table.Field;
import Pandemic.Table.Table;

public class Application {
    public static void main(String[] args){
        Game game = new Game();

        game.setSaver(new Saver<Game>(game, "pandemic.restore.txt"));
        int numOfPlayers = 2;
        Player[] players = new Player[numOfPlayers];
        players[0] = new ConsolePlayer(game, System.in, System.out);
        players[1] = new ConsolePlayer(game, System.in, System.out);

        game.start(players, 3);
    }
}
