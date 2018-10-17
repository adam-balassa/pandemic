package Pandemic.Cards;

import Pandemic.Exceptions.EndOfGame;
import Pandemic.Players.Player;
import Pandemic.Virus;

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

    abstract public void draw(Player p) throws EndOfGame;

    boolean isEvent(){ return false; }

    public Virus getColor(){ return null; }
}
