package Pandemic;

import Pandemic.Cards.*;
import Pandemic.Exceptions.EndOfGame;

import java.util.List;

public interface Events {
    void dontInfect();
    List<CityCard> forecast();
    List<CityCard> getTrash();
    void epidemic() throws EndOfGame;
    void replaceCards(List<CityCard> c);
}
