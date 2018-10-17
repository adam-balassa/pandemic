package Pandemic.Cards;

import java.util.Collection;
import java.util.List;

public class InfectionDeck extends Deck{
    public InfectionDeck(Collection<Card> cards) {
        super(cards);
    }

    public void concat(InfectionTrash trash){
        trash.shuffle();
        List<? extends Card> cards = trash.getCards();
        for (Card card: cards) deck.push(card);
    }

    public CityCard drawLast(){
        CityCard card = (CityCard) deck.get(deck.size() - 1);
        deck.remove(deck.size() - 1);
        return card;
    }
}
