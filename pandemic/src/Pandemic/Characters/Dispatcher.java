package Pandemic.Characters;

import Pandemic.Cards.Card;
import Pandemic.Exceptions.AmbigousAction;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Core.Hand;
import Pandemic.Table.Field;

public class Dispatcher extends Character {
    public Dispatcher(Hand h, Field f) {
        super(CharacterType.Dispatcher, h, f);
    }

    @Override
    public int move(Field f, Character c) throws CannotPerformAction {
        if(c.getField().hasNeighbour(f)){
            c.replace(f);
            return 1;
        }

        if(this.field.hasStation() && f.hasStation()){
            c.replace(f);
            return 1;
        }

        if(f.getCharacters().size() != 0){
            c.replace(f);
            return 1;
        }

        Card c1 = hand.hasCard(c.getField());
        Card c2 = hand.hasCard(f);

        if(c1 != null && c2 != null) throw new AmbigousAction("You can take either private or public plane");

        if(c1 != null) {
            c.replace(f);
            hand.drop(c1);
            return 1;
        }

        if(c2 != null){
            c.replace(f);
            hand.drop(c2);
            return 1;
        }

        throw new CannotPerformAction("You are not available to move to" + f.getName());
    }
}
