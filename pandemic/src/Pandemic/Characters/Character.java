package Pandemic.Characters;

import Pandemic.Cards.Card;
import Pandemic.Cards.CityCard;
import Pandemic.Cards.EventCard;
import Pandemic.Exceptions.*;
import Pandemic.Core.Hand;
import Pandemic.Players.Player;
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

    /**
     * Moves the character to the specified field
     * @param f the field where the character shall move
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction
     */
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

    /**
     * Moves the specified character to the specified field
     * A regular character is not capable of performing this action
     * @param f the field where the given character shall be moved to
     * @param c the character that is to be moved
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction
     */
    @Override
    public int move(Field f, Character c) throws CannotPerformAction{
        throw new CharacterCannotPerformAction("Your character doesn't have the ability to move other players");
    }

    /**
     * Removes a virus from the field that the character stands on
     * @param v the color of the virus that the user wants to remove
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction when there's no virus of the given color on the field
     */
    @Override
    public int clean(Virus v) throws CannotPerformAction{
        if(hand.isAntidoteMade(v)){
            field.clearAll(v);
            return 1;
        }
        field.clear(v);
        return 1;
    }

    /**
     * Removes a virus same to the field's color
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction
     */
    @Override
    public int clean() throws CannotPerformAction{
        return clean(this.field.getColor());
    }

    /**
     * Builds a research station on the field that the character stands on
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction
     */
    @Override
    public int build() throws CannotPerformAction{
        Card c = this.hand.hasCard(this.field);
        if(c == null) throw new CannotPerformAction("You need " + field.getName() + " card to build a station");
        this.field.build();
        hand.drop(c);
        return 1;
    }

    /**
     * Creates an antidote
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction when the character isn't standing on a research station or doesn't have enough cards
     */
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

    /**
     * Draws an event from the main trash
     * Only Contingency Planners are allowed to perform this action
     * @param card the card that is to be drawn
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction
     */
    @Override
    public int getEvent(EventCard card) throws CannotPerformAction{
        throw new CharacterCannotPerformAction("Your character doesn't have the ability to get event cards from trash");
    }

    /**
     * Gives the specified card to the specified character
     * @param c character
     * @param card
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction when the two characters aren't standing on the proper field
     */
    @Override
    public int giveCard(Character c, CityCard card) throws CannotPerformAction {
        if(card.getCity() != this.field || !this.field.getCharacters().contains(c))
            throw new CannotPerformAction("You must stand on the same field to change cards");
        hand.remove(card);
        c.recieveCard(card);
        return 1;
    }

    /**
     * Plays the specified event
     * @param e the event card that is to be played
     * @throws CannotPerformAction
     */
    @Override
    public void playEvent(EventCard e) throws CannotPerformAction {
        hand.playEvent(e);
    }

    /**
     * Called when the character recieves a card from an other
     * @param card
     */
    final void recieveCard(CityCard card){
        hand.add(card);
    }

    /**
     * Modifies the character's position on the board
     * @param f
     * @throws UnnecessaryAction
     */
    public void replace(Field f) throws UnnecessaryAction{
        if(field == f) throw new UnnecessaryAction("Your character is already in " + f.getName());
        field.stepFrom(this);
        field = f;
        field.step(this);
    }


    /**
     * Groups the player's cards by their colors
     * @param number
     * @returns the groupped cards if their amount matches the given number
     */
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
