package Pandemic.Cards;

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
        for(Card card : deck){
            if(card.isEvent()) cards.add((EventCard) card);
        }
        return cards;
    }

    public void drawEvent(EventCard c){
        for(Iterator<Card> i = deck.listIterator(); i.hasNext();){
            if(i.next() == c) {
                i.remove();
                return;
            }
        }
    }

}
