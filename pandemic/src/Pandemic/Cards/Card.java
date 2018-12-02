package Pandemic.Cards;

import Pandemic.Exceptions.EndOfGame;
import Pandemic.Players.Player;
import Pandemic.Core.Virus;
import Pandemic.View.Components.CardComponent;

import java.io.Serializable;


abstract public class Card implements Serializable {
    private String name;
    public int index;
    private static int i = 0;

    protected Card(String n){
        name = n;
        i++;
        this.index = i;
    }

    public String getName(){ return name; }

    /**
     * A card knows what to do, when it's being drawn
     * @param p the player who drew this card
     * @throws EndOfGame if it was an epidemic card which ended the game
     */
    abstract public void draw(Player p) throws EndOfGame;

    /**
     * A getter if the card can be played any time
     * @return
     */
    boolean isEvent(){ return false; }

    /**
     * Returns it's color if it has one
     * @return
     */
    public Virus getColor(){ return null; }

    /**
     * Returns a JavaFX Pane that can render this card
     * @return
     */
    abstract public CardComponent getDrawer();
}
