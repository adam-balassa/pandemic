package Pandemic.Players;

import Pandemic.Cards.Card;
import Pandemic.Cards.CityCard;
import Pandemic.Cards.EventCard;
import Pandemic.Core.Events;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Exceptions.EndOfGame;
import Pandemic.Exceptions.EndRound;
import Pandemic.Exceptions.UnnecessaryAction;
import Pandemic.Core.Hand;
import Pandemic.Core.IGame;
import Pandemic.Table.Field;
import Pandemic.Core.Virus;
import Pandemic.Characters.Character;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Player implements Hand, Events, Serializable {
    public Character character;
    protected List<Card> cards;


    transient protected IGame game;
    transient protected Card chosenCard;
    transient private int actions = 0;
    transient private String name;

    private static int players = 1;


    /**
     * Default constructor
     * sets players name automatically
     */
    protected Player(IGame g){
        this.cards = new ArrayList<>();
        this.name = "Player " + players;
        this.chosenCard = null;
        this.game = g;
        players++;
    }

    /**
     * Sets players name dynamically
     */
    protected Player(IGame g, String name){
        this(g);
        this.name = name;
    }

    public int getRemainingActions(){
        return this.actions;
    }

    public void setCharacter(Character c){
        character = c;
    }

    public void reconstruct(Player previousStatus){
        cards = previousStatus.cards;
        character = previousStatus.character;
        character.setHand(this);
        actions = 4;
        chosenCard = null;
    }

    /**
     * abstract function for initializing a new round
     */
    public void round() throws EndOfGame {
        actions = 4;
    }

    /**
     * Callback for the end of a round
     */
    public void endRound() throws EndOfGame {
        game.endRound();
    }

    public void draw(Card c){
        this.add(c);
    }

    public void finish(){
        while(this.cards.size() > 7)
            hasToDrop();
    }

    abstract protected void hasToDrop();
    abstract public void showInfection(CityCard card);

    /**
     * helper functions for a player
     */
    protected Card hasCard(String name){
        if(chosenCard != null && !chosenCard.getName().equals(name)) return null;
        for (Card c : cards) {
            if(c.getName().equals(name)) return c;
        }
        return null;
    }

    protected void useAction(int i) throws EndRound{
        if(i == 0) return;
        actions -= i;
        if(actions == 0) throw new EndRound();
    }

    /**********************
     ******* EVENTS *******
     *********************/
    @Override
    public void dontInfect() {
        game.dontInfect();
    }

    @Override
    abstract public List<CityCard> forecast();

    @Override
    abstract public List<CityCard> getTrash();

    @Override
    abstract public void replaceCards(List<CityCard> c);

    @Override
    public void epidemic() throws EndOfGame{
        game.epidemic();
    }

    /**********************
     *******  HAND  *******
     *********************/
    @Override
    public List<Card> getCards() {
        if(chosenCard != null){
            List<Card> card = new ArrayList<>(1);
            card.add(chosenCard);
            return card;
        }
        return this.cards;
    }

    @Override
    public void add(Card c) {
        this.cards.add(c);
    }

    @Override
    public void remove(Card c) throws CannotPerformAction {
        if(this.cards.remove(c)){
            if(chosenCard == c) chosenCard = null;
        }
        else throw new CannotPerformAction("Player doesn't have the " + c.getName() + " card");
    }

    @Override
    public void drop(Card c) throws CannotPerformAction {
        this.remove(c);
        game.drop(c);
    }

    @Override
    public void playEvent(EventCard c) throws CannotPerformAction {
        this.drop(c);
    }

    @Override
    public Card hasCard(Field f) {
        if(f == null) return null;
        return hasCard(f.getName());
    }

    @Override
    public boolean isAntidoteMade(Virus v) {
        return game.isAntidoteMade(v);
    }

    @Override
    public void createAntidote(Virus v) throws UnnecessaryAction {
        game.createAntidote(v);
    }

    // getter
    public String getName(){ return name; }
}
