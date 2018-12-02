package Pandemic.Characters;

import Pandemic.Cards.CityCard;
import Pandemic.Core.Game;
import Pandemic.Core.Virus;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Players.ConsolePlayer;
import Pandemic.Players.Player;
import Pandemic.Table.Table;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScientistTest {

    private Player hand;
    private Character scientist;
    private Table table;
    private Game game;

    public ScientistTest(){}

    private void init(){
        game = new Game(null);
        hand = new ConsolePlayer(game, System.in, System.out);
        game.setPlayers(new Player[]{hand});
        game.initialize(3);
        table = new Table();
        scientist = new Scientist(hand, table.getField("Atlanta"));
    }
    @Test
    public void testAntidote() throws CannotPerformAction {
        init();
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Atlanta"), 0));

        scientist.build();
        int i = scientist.antidote();
        Assert.assertEquals("Didn't count action",  i, 1);
        Assert.assertNull("Didn't drop card", hand.hasCard(table.getField("Chicago")));
        Assert.assertTrue("Didn't create antidote", game.getAntidotes().contains(Virus.BLUE));
    }
}