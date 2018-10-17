package Pandemic.Cards;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MainTrash extends Deck{
    public MainTrash(Collection<Card> cards) {
        super(cards);
    }

    public List<EventCard> getEvents(){
        List<EventCard> cards = new ArrayList<>();
        for(Iterator<Card> i = deck.listIterator(); i.hasNext();){
            Card card = i.next();
            if(card.isEvent()) cards.add((EventCard) card);
        }
        return cards;
    }

    public void drawEvent(EventCard c){
        for(Iterator<Card> i = deck.listIterator(); true;){
            if(i.next() == c) i.remove();
        }
    }

}
