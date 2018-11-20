package Pandemic.Cards;

import java.util.*;

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
        for(Iterator<Card> card = deck.listIterator(); true;){
            if(card.next() == c) {
                card.remove();
                return;
            }
        }
    }

    public void empty(){ deck.clear(); }

}
