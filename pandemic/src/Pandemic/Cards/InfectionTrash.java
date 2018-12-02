package Pandemic.Cards;

import java.util.*;

public class InfectionTrash extends Deck {
    public InfectionTrash(Collection<Card> cards) {
        super(cards);
    }

    /**
     * Returns all the CityCards in the deck
     * @return
     */
    public List<CityCard> getCards(){
        List<CityCard> cards = new ArrayList<>();
        for(Card card: deck) cards.add((CityCard) card);
        return cards;
    }

    /**
     * One can draw any card from any position from an infection trash
     * @param c
     */
    public void removeCard(Card c){
        for(Iterator<Card> card = deck.listIterator(); true;){
            if(card.next() == c) {
                card.remove();
                return;
            }
        }
    }

    /**
     * deletes all cards from the deck
     */
    public void empty(){ deck.clear(); }

}
