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

    public void shuffle(){
        Collections.shuffle(deck);
    }

    public Card draw() throws EndOfGame{
        if(deck.empty()) throw new EndOfGame("Deck ran out");
        return deck.pop();
    }

    public void add(Card c){
        deck.push(c);
    }
}
