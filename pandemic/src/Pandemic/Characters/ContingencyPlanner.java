package Pandemic.Characters;

import Pandemic.Cards.EventCard;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Core.Hand;
import Pandemic.Table.Field;

import java.io.Serializable;

public class ContingencyPlanner extends Character implements Serializable {

    private EventCard eventCard;

    public ContingencyPlanner(Hand h, Field f) {
        super(CharacterType.ContingencyPlanner, h, f);
        eventCard = null;
    }

    /**
     * A Contingency Planner is able to draw event cards from the main trash
     * @param card the card that is to be drawn
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction
     */
    @Override
    public int getEvent(EventCard card) throws CannotPerformAction {
        if(eventCard != null) throw new CannotPerformAction("You already have an event card");
        eventCard = card;
        hand.add(card);
        return 1;
    }

    @Override
    public void playEvent(EventCard e) throws CannotPerformAction {
        if(e == eventCard){
            eventCard = null;
        }
        super.playEvent(e);
    }
}
