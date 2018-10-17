package Pandemic.Cards;

import Pandemic.Exceptions.EndOfGame;
import Pandemic.Players.Player;

public class EpidemicCard extends Card{
    public EpidemicCard(){
        super("Epidemic");
    }

    public void draw(Player p) throws EndOfGame{
        p.epidemic();
    }
}
