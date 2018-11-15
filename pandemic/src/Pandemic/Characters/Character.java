package Pandemic.Characters;

import Pandemic.Cards.Card;
import Pandemic.Cards.CityCard;
import Pandemic.Cards.EventCard;
import Pandemic.Exceptions.*;
import Pandemic.Core.Hand;
import Pandemic.Table.Field;
import Pandemic.Core.Virus;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

abstract public class Character implements Actions, Serializable {
    protected CharacterType character;
    protected Field field;
    protected Hand hand;

    protected Character(CharacterType c, Hand h, Field f){
        this.setHand(h);
        character = c;
        field = f;
        field.step(this);
    }

    public void setHand(Hand h){
        hand = h;
    }
    @Override
    public int move(Field f) throws CannotPerformAction{
        if(field.hasNeighbour(f)){
            this.replace(f);
            return 1;
        }

        if(this.field.hasStation() && f.hasStation()){
            this.replace(f);
            return 1;
        }

        Card c1 = hand.hasCard(this.field);
        Card c2 = hand.hasCard(f);

        if(c1 != null && c2 != null) throw new AmbigousAction("You can take either private or public plane");

        if(c1 != null) {
            replace(f);
            hand.drop(c1);
            return 1;
        }

        if(c2 != null){
            replace(f);
            hand.drop(c2);
            return 1;
        }

        throw new CannotPerformAction("You are not available to move to " + f.getName());
    }

    @Override
    public int move(Field f, Character c) throws CannotPerformAction{
        throw new CharacterCannotPerformAction("Your character doesn't have the ability to move other players");
    }

    @Override
    public int clean(Virus v) throws CannotPerformAction{
        if(hand.isAntidoteMade(v)){
            field.clearAll(v);
            return 1;
        }
        field.clear(v);
        return 1;
    }

    @Override
    public int clean() throws CannotPerformAction{
        return clean(this.field.getColor());
    }

    @Override
    public int build() throws CannotPerformAction{
        Card c = this.hand.hasCard(this.field);
        if(c == null) throw new CannotPerformAction("You need " + field.getName() + " card to build a station");
        this.field.build();
        hand.drop(c);
        return 1;
    }

    @Override
    public int antidote()throws CannotPerformAction{
        if(!this.field.hasStation()) throw new CannotPerformAction("You can only create antidote in a research station");

        ArrayList<Card> cards = (ArrayList<Card>) this.similarCards(5);
        if(cards == null) throw new CannotPerformAction("You need 5 cards with matching colors to create an antidote");

        hand.createAntidote(cards.get(0).getColor());
        for (Card card: cards) {
            hand.drop(card);
        }
        return 1;
    }

    @Override
    public int getEvent(EventCard card) throws CannotPerformAction{
        throw new CharacterCannotPerformAction("Your character doesn't have the ability to get event cards from trash");
    }

    @Override
    public int giveCard(Character c, CityCard card) throws CannotPerformAction {
        if(card.getCity() != this.field || !this.field.getCharacters().contains(c))
            throw new CannotPerformAction("You must stand on the same field to change cards");
        hand.remove(card);
        c.recieveCard(card);
        return 1;
    }

    @Override
    public void playEvent(EventCard e) throws CannotPerformAction {
        hand.playEvent(e);
    }

    final void recieveCard(CityCard card){
        hand.add(card);
    }

    public void replace(Field f) throws UnnecessaryAction{
        if(field == f) throw new UnnecessaryAction("Your character is already in " + f.getName());
        field.stepFrom(this);
        field = f;
        field.step(this);
    }

    protected Collection<Card> similarCards(int number){
        HashMap<Virus, Collection<Card>> colors = new HashMap<>();

        colors.put(Virus.BLACK, new ArrayList<>(number));
        colors.put(Virus.RED, new ArrayList<>(number));
        colors.put(Virus.YELLOW, new ArrayList<>(number));
        colors.put(Virus.BLUE, new ArrayList<>(number
        ));

        for (Card card : hand.getCards()) {
            Virus color = card.getColor();
            if( color != null ){
                Collection<Card> cards = colors.get(color);
                cards.add(card);
                if(cards.size() == number) return cards;
            }
        }

        return null;
    }

    public Field getField() {
        return field;
    }

    public String getName(){
        return this.character.name;
    }

    public boolean equals(String s) {
        return character.name.equals(s);
    }

    public Color getColor(){
        return this.character.color;
    }
}
