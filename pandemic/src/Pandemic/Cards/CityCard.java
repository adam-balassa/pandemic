package Pandemic.Cards;

import Pandemic.Players.Player;
import Pandemic.Table.Field;
import Pandemic.Core.Virus;

import java.io.Serializable;

public class CityCard extends Card implements Comparable<CityCard>, Serializable {
    int population;
    Field city;

    public CityCard(Field f, int p){
        super(f.getName());
        population = p;
        city = f;
    }

    public Field getCity() {
        return city;
    }

    @Override
    public void draw(Player p){
        p.draw(this);
    }

    @Override
    public int compareTo(CityCard c){
        return this.population - c.population;
    }

    @Override
    public Virus getColor() {
        return city.getColor();
    }
}
