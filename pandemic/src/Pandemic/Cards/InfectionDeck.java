package Pandemic.Cards;

import java.util.Collection;
import java.util.List;

public class InfectionDeck extends Deck{
    public InfectionDeck(Collection<Card> cards) {
        super(cards);
    }

    /**
     * Called when the infectiontrash shall be reshuffled into the infection deck.
     * The shuffled cards are concatted to the top of the deck
     * @param trash
     */
    public void concat(InfectionTrash trash){
        trash.shuffle();
        List<? extends Card> cards = trash.getCards();
        for (Card card: cards) deck.push(card);
    }

    /**
     * Called when a card shall be drawn from the bottom of the deck (only when epidemic happens)
     * @return
     */
    public CityCard drawLast(){
        CityCard card = (CityCard) deck.get(deck.size() - 1);
        deck.remove(deck.size() - 1);
        return card;
    }
}
