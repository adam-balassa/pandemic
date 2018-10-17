package Pandemic.Characters;

import Pandemic.Cards.CityCard;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Hand;
import Pandemic.Table.Field;

public class Researcher extends Character {
    public Researcher(Hand h, Field f) {
        super(CharacterType.Researcher, h, f);
    }

    @Override
    public int giveCard(Character c, CityCard card) throws CannotPerformAction {
        if(!this.field.getCharacters().contains(c))
            throw new CannotPerformAction("You must stand on the same field to change cards");
        hand.remove(card);
        c.recieveCard(card);
        return 1;
    }
}
