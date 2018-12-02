package Pandemic.Cards;

import Pandemic.Exceptions.EndOfGame;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;

public class Deck implements Serializable {
    protected Stack<Card> deck;

    public Deck(Collection<Card> cards){
        deck = new Stack<>();
        for (Card card: cards )
            deck.push(card);
    }

    /**
     * Shuffles the deck
     */
    public void shuffle(){
        Collections.shuffle(deck);
    }

    /**
     * Draws a card from the top of the deck
     * @returns the card that was drawn and deletes it from the deck
     * @throws EndOfGame if the deck ran out
     */
    public Card draw() throws EndOfGame{
        if(deck.empty()) throw new EndOfGame("Deck ran out");
        return deck.pop();
    }

    /**
     * Places the given card to the top of the deck
     * @param c the card that is to be added
     */
    public void add(Card c){
        deck.push(c);
    }
}
