package Pandemic.Characters;

import Pandemic.Cards.EventCard;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Hand;
import Pandemic.Table.Field;

import java.io.Serializable;

public class ContingencyPlanner extends Character implements Serializable {

    private EventCard eventCard;

    public ContingencyPlanner(Hand h, Field f) {
        super(CharacterType.ContingencyPlanner, h, f);
        eventCard = null;
    }

    @Override
    public int getEvent(EventCard card) throws CannotPerformAction {
        return super.getEvent(card);
    }

    @Override
    public void playEvent(EventCard e) throws CannotPerformAction {
        if(e == eventCard){
            hand.add(e);
            eventCard = null;
        }
        super.playEvent(e);
    }
}
