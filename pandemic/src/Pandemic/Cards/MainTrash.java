package Pandemic.Cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MainTrash extends Deck{
    public MainTrash(Collection<Card> cards) {
        super(cards);
    }

    /**
     * @returns the Event Cards from the deck
     */
    public List<EventCard> getEvents(){
        List<EventCard> cards = new ArrayList<>();
        for(Card card : deck){
            if(card.isEvent()) cards.add((EventCard) card);
        }
        return cards;
    }

    /**
     * An event card can be drawn from any position
     * @param c
     */
    public void drawEvent(EventCard c){
        for(Iterator<Card> i = deck.listIterator(); i.hasNext();){
            if(i.next() == c) {
                i.remove();
                return;
            }
        }
    }

}
