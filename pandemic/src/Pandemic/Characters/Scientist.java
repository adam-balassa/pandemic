package Pandemic.Characters;

import Pandemic.Cards.Card;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Core.Hand;
import Pandemic.Table.Field;

import java.util.ArrayList;

public class Scientist extends Character{
    public Scientist(Hand h, Field f) {
        super(CharacterType.Scientist, h, f);
    }

    @Override
    public int antidote() throws CannotPerformAction {
        if(!this.field.hasStation()) throw new CannotPerformAction("You can only create antidote in a research station");

        ArrayList<Card> cards = (ArrayList<Card>) this.similarCards(4);
        if(cards == null) throw new CannotPerformAction("You need 5 cards with matching colors to create an antidote");

        hand.createAntidote(cards.get(0).getColor());
        for (Card card: cards) {
            hand.drop(card);
        }
        return 1;
    }
}
