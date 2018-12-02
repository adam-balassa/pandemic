package Pandemic.Characters;

import static org.junit.Assert.*;

import Pandemic.Cards.CityCard;
import Pandemic.Cards.EventCard;
import Pandemic.Core.Game;
import Pandemic.Core.Virus;
import Pandemic.Exceptions.AmbigousAction;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Exceptions.UnnecessaryAction;
import Pandemic.Players.ConsolePlayer;
import Pandemic.Players.Player;
import Pandemic.Table.*;
import org.junit.Assert;
import org.junit.Test;

public class ResearcherTest {
    private Player hand;
    private Character researcher;
    private Table table;
    private Game game;

    public ResearcherTest(){
        game = new Game(null);
        hand = new ConsolePlayer(game, System.in, System.out);
        game.setPlayers(new Player[]{hand});
        game.initialize(3);
        table = new Table();
        researcher = new Researcher(hand, table.getField("Atlanta"));
    }

    private void init(){
        game = new Game(null);
        hand = new ConsolePlayer(game, System.in, System.out);
        game.setPlayers(new Player[]{hand});
        game.initialize(3);
        table = new Table();
        researcher = new Researcher(hand, table.getField("Atlanta"));
    }

    @Test
    public void testMove() throws CannotPerformAction{
        init();
        Field chicago = table.getField("Chicago");
        int i = researcher.move(chicago);
        Assert.assertEquals("Didn't count action",  i, 1);
        Assert.assertEquals("Didn't save new Position", researcher.getField(), chicago);
        Assert.assertEquals("Field didn't save new Character", chicago.getCharacters().get(0), researcher);
    }

    @Test
    public void testPrivateJet() throws CannotPerformAction{

        init();
        hand.add(new CityCard(table.getField("Atlanta"), 0));
        Field sidney = table.getField("Sidney");
        int i = researcher.move(sidney);
        Assert.assertEquals("Didn't count action",  i, 1);
        Assert.assertEquals("Didn't save new Position", researcher.getField(), sidney);
        Assert.assertEquals("Field didn't save new Character", sidney.getCharacters().get(0), researcher);
        Assert.assertNull("Didn't drop card", hand.hasCard(table.getField("Atlanta")));
    }

    @Test
    public void testPublicJet() throws CannotPerformAction{

        init();
        hand.add(new CityCard(table.getField("Sidney"), 0));
        Field sidney = table.getField("Sidney");
        int i = researcher.move(sidney);
        Assert.assertEquals("Didn't count action",  i, 1);
        Assert.assertEquals("Didn't save new Position", researcher.getField(), sidney);
        Assert.assertEquals("Field didn't save new Character", sidney.getCharacters().get(0), researcher);
        Assert.assertNull("Didn't drop card", hand.hasCard(table.getField("Sidney")));
    }

    @Test (expected= AmbigousAction.class)
    public void testAmbiguousAction() throws CannotPerformAction{
        init();
        hand.add(new CityCard(table.getField("Sidney"), 0));
        hand.add(new CityCard(table.getField("Atlanta"), 0));
        Field sidney = table.getField("Sidney");
        researcher.move(sidney);
    }

    @Test (expected= CannotPerformAction.class)
    public void testMoveOtherPlayers() throws CannotPerformAction{
        init();
        Field chicago = table.getField("Chicago");
        researcher.move(chicago, null);
    }

    @Test (expected= UnnecessaryAction.class)
    public void testClean() throws CannotPerformAction{
        init();
        researcher.clean();
    }

    @Test
    public void testBuild() throws CannotPerformAction{
        init();
        hand.add(new CityCard(table.getField("Chicago"), 0));
        researcher.move(table.getField("Chicago"));
        int i = researcher.build();
        Assert.assertEquals("Didn't count action",  i, 1);
        Assert.assertNull("Didn't drop card", hand.hasCard(table.getField("Chicago")));
        Assert.assertTrue("Didn't build station", table.getField("Chicago").hasStation());
    }

    @Test
    public void testAntidote() throws CannotPerformAction{
        init();
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Atlanta"), 0));

        researcher.build();
        int i = researcher.antidote();
        Assert.assertEquals("Didn't count action",  i, 1);
        Assert.assertNull("Didn't drop card", hand.hasCard(table.getField("Chicago")));
        Assert.assertTrue("Didn't create antidote", game.getAntidotes().contains(Virus.BLUE));
    }

    @Test (expected=CannotPerformAction.class)
    public void testAntidote2() throws CannotPerformAction{
        init();
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Chicago"), 0));
        hand.add(new CityCard(table.getField("Atlanta"), 0));

        researcher.build();
        int i = researcher.antidote();
    }

    @Test(expected=CannotPerformAction.class)
    public void testGetCard() throws CannotPerformAction{
        init();
        researcher.getEvent(new EventCard(EventCard.EventTypes.RESILENTPPULATION));
    }
}