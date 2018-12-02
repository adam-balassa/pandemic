package Pandemic.Characters;

import Pandemic.Cards.EventCard;
import Pandemic.Core.Game;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Players.ConsolePlayer;
import Pandemic.Players.Player;
import Pandemic.Table.Table;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ContingencyPlannerTest {
    private Player hand;
    private Character contingencyPlanner;
    private Table table;
    private Game game;

    public ContingencyPlannerTest(){}

    private void init(){
        game = new Game(null);
        hand = new ConsolePlayer(game, System.in, System.out);
        game.setPlayers(new Player[]{hand});
        game.initialize(3);
        table = new Table();
        contingencyPlanner = new ContingencyPlanner(hand, table.getField("Atlanta"));
    }

    @Test
    public void testGetCard() throws CannotPerformAction{
        init();
        contingencyPlanner.getEvent(new EventCard(EventCard.EventTypes.RESILENTPPULATION));
    }
}