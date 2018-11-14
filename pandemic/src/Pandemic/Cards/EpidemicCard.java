package Pandemic.Cards;

import Pandemic.Exceptions.EndOfGame;
import Pandemic.Players.Player;
import Pandemic.View.Components.CardComponent;

public class EpidemicCard extends Card{
    public EpidemicCard(){
        super("Epidemic");
    }

    public void draw(Player p) throws EndOfGame{
        p.epidemic();
    }

    @Override
    public CardComponent getDrawer() {
        return null;
    }
}
