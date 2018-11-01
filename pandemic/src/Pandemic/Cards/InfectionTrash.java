package Pandemic.Cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class InfectionTrash extends Deck {
    public InfectionTrash(Collection<Card> cards) {
        super(cards);
    }

    public List<CityCard> getCards(){
        List<CityCard> cards = new ArrayList<>();
        for(Card card: deck) cards.add((CityCard) card);
        return cards;
    }

    public void removeCard(Card c){
        for(Iterator<Card> i = deck.listIterator(); true;){
            if(i.next() == c) i.remove();
        }
    }

    public void empty(){ deck.clear(); }

}
