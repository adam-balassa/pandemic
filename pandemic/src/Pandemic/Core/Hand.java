package Pandemic.Core;

import Pandemic.Cards.Card;
import Pandemic.Cards.EventCard;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Exceptions.UnnecessaryAction;
import Pandemic.Table.Field;

import java.util.List;

public interface Hand {
    List<Card> getCards();
    void add(Card c);
    void remove(Card c) throws CannotPerformAction;
    void drop(Card c) throws CannotPerformAction;
    void playEvent(EventCard c) throws CannotPerformAction;
    Card hasCard(Field f);
    boolean isAntidoteMade(Virus v);
    void createAntidote(Virus v) throws UnnecessaryAction;
}
