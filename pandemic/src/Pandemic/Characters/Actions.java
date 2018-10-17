package Pandemic.Characters;

import Pandemic.Cards.CityCard;
import Pandemic.Cards.EventCard;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Table.Field;
import Pandemic.Virus;


public interface Actions {
    int move(Field f) throws CannotPerformAction;
    int move(Field f, Character c) throws CannotPerformAction;
    int clean(Virus v) throws CannotPerformAction;
    int clean() throws CannotPerformAction;
    int build() throws CannotPerformAction;
    int antidote() throws CannotPerformAction;
    int getEvent(EventCard e) throws CannotPerformAction;
    int giveCard(Character c, CityCard card) throws CannotPerformAction;
    void playEvent(EventCard e) throws CannotPerformAction;
}
