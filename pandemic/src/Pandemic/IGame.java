package Pandemic;

import Pandemic.Cards.Card;
import Pandemic.Cards.EventCard;
import Pandemic.Characters.Character;
import Pandemic.Exceptions.EndOfGame;
import Pandemic.Exceptions.UnnecessaryAction;
import Pandemic.Table.Field;

import java.util.List;
import java.util.Map;

public interface IGame extends Events {
    void drop(Card c);
    boolean isAntidoteMade(Virus v);
    void endRound();
    Character getCharacter(String name);
    void createAntidote(Virus color) throws UnnecessaryAction;
    void addBreakOuts(int breakOuts) throws EndOfGame; //BUG nincs rá itt szükség
    Field getField(String cityName);
    List<EventCard> getEvents();
    void getEvent(EventCard eventCard);
    Map<String, Field> getFields();
    void undo();
}
