package Pandemic.Players;

import Pandemic.Cards.*;
import Pandemic.Characters.Character;
import Pandemic.Core.Events;
import Pandemic.Core.IGame;
import Pandemic.Core.Pandemic;
import Pandemic.Exceptions.*;
import Pandemic.Table.Field;
import Pandemic.View.Components.CardComponent;
import Pandemic.View.Scenes.GameScene;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GraphicsPlayer extends Player implements Serializable {

    /**
     * The graphics interface of the game
     */
    transient private GameScene graphics;
    /**
     * modified when a user interaction shall be a specified series of events
     */
    transient private State waitingState;
    /**
     * The specification of an event that is being played
     */
    transient private EventCard.EventOptions setEventOptions;
    /**
     * The specification of an interaction that includes a series of events
     */
    transient private InteractionOptions setInteractionOptions;
    /**
     * The type of interaction that is in progress
     */
    transient private Interaction setInteraction;
    /**
     * A flag for an event if it's done
     */
    transient private boolean eventReady;
    private boolean onRound = false;

    /**
     * A collection of cards that were drawn at the end of the round
     */
    private List<Card> drawnCards;
    private List<Card> infectedCards = new ArrayList<>(4);

    public GraphicsPlayer(IGame g, GameScene graphicsContext) {
        super(g);
        graphics = graphicsContext;
        waitingState = State.NULL;
        setEventOptions = new EventCard.EventOptions.Builder().build();
        setInteractionOptions = new InteractionOptions.Builder().build();
    }

    @Override
    public void round() throws EndOfGame {
        super.round();
        drawnCards =  new ArrayList<>(2);
        infectedCards = new ArrayList<>(4);
        graphics.newRound(this);
        graphics.message("New round started for " + character.getName());
        onRound = true;
    }

    @Override
    protected void hasToDrop() {
        waitingState = State.FINISHROUND;
        graphics.message("You need to drop a card");
    }

    @Override
    public void showInfection(CityCard card, int maxInfection) {
        if(!onRound) return;
        infectedCards.add(card);
        if(infectedCards.size() == maxInfection)
            graphics.draw(drawnCards, infectedCards);
    }

    @Override
    public List<CityCard> forecast() {
        graphics.openInfectionTrash(game.forecast(), Interaction.INFECTIONDECKCARDCLICK);
        eventReady = false;
        return null;
    }

    private void manageForecast(CityCard c){
        List<CardComponent> components = graphics.getHand().getCards();
        List<CityCard> forecastCards = new ArrayList<>(6);
        for(int i = components.size(); i > 0; i--) forecastCards.add((CityCard) components.get(i-1).getCard());
        for(CityCard card: forecastCards)
            if(c.getName().equals(card.getName())){
                forecastCards.remove(card);
                break;
            }
        forecastCards.add(c);
        replaceCards(forecastCards);
        graphics.removeHand();
        graphics.openInfectionTrash(game.forecast(), Interaction.INFECTIONDECKCARDCLICK);
    }

    @Override
    public InfectionTrash getTrash() {
        eventReady = false;
        graphics.openInfectionTrash(game.getTrash().getCards(), Interaction.INFECTIONTRASHCARDCLICK);
        return null;
    }

    @Override
    public void replaceCards(List<CityCard> c) {
        game.replaceCards(c);
    }

    @Override
    public void endRound() throws EndOfGame {
        try{
            super.endRound();
            onRound = false;
        }
        catch (PandemicException e){
            graphics.endGame();
        }
    }

    @Override
    public void add(Card c) {;
        super.add(c);
        if(!onRound) return;
        drawnCards.add(c);
    }

    @Override
    public void epidemic() throws EndOfGame {
        super.epidemic();
        if(!onRound) return;
        drawnCards.add(new EpidemicCard());
    }

    @Override
    public void finish() {
        if(cards.size() > 7)
            hasToDrop();
        else {
            waitingState = State.NULL;
            try {
                game.finishRound();
                game.nextRound();
            } catch (EndOfGame endOfGame) {
                graphics.endGame();
            }
        }
    }

    /**
     * Called when any important user interaction happened on the graphics interface.
     * @param interaction the type of interaction that happened
     * @param options specifications of the given interactions
     */
    public void action(Interaction interaction, InteractionOptions options){
        int usedActions = 0;
        if(interaction == Interaction.RESTARTCLICK) {
            game.undo();
            graphics.newRound(this);
            graphics.restart();
            return;
        }
        try{
            switch (waitingState){
                case NULL:
                    switch(interaction){
                        case FIELDCLICK:
                            options.check(true, false, false, false);
                            usedActions = character.move(options.field);
                            break;
                        case CARDCLICK:
                            options.check(false, false, false, true);
                            try{
                                EventCard event = (EventCard) this.hasCard(options.card.getName());
                                if(event == null) throw new InvalidParameter("You don't have a card called" + options.card.getName());
                                setEventOptions = new EventCard.EventOptions.Builder().setPlayer(this).build();
                                this.waitingState = State.EVENTSPECIFICATION;
                                character.playEvent(event);
                            }catch(Exception e){
                                throw new CannotPerformAction(options.card.getName() + " is not an event card");
                            }
                            break;
                        case OTHERCARDCLICK:
                            options.check(false, false, true, true);
                            usedActions += options.player.character.giveCard(this.character, (CityCard) options.card); //TODO: átgondolni
                            break;
                        case TREATCLCIK:
                            usedActions += character.clean();
                            break;
                        case BUILDCLICK:
                            usedActions += character.build();
                            break;
                        case ANTIDOTECLICK:
                            usedActions += character.antidote();
                            break;
                        case OUTCLICK:
                            reset();
                            break;
                        case SHARECLICK:
                            waitingState = State.SHARECARD;
                            graphics.message("Choose the card you'd like to share");
                            break;
                        case PASSCLICK:
                            this.endRound();
                            break;
                        case CHARACTERCLICK:
                            options.check(false, true, false, false);
                            waitingState = State.ACTIONSPECIFICATION;
                            setInteractionOptions = options;
                            setInteraction = interaction;
                            graphics.message("Choose a field, you'd like to move this character");
                            break;
                        case INFECTIONTRASHBUTTONCLICK:
                            graphics.openInfectionTrash(game.getTrash().getCards(), Interaction.INFECTIONTRASHCARDCLICK);
                            break;
                        case MAINTRASHBUTTONCLICK:
                            graphics.openInfectionTrash(game.getEvents(), Interaction.MAINTRASHCARDCLICK);
                            break;
                        case MAINTRASHCARDCLICK:
                            options.check(false, false, false, true);
                            usedActions += character.getEvent((EventCard)options.card);
                            game.drawEvent((EventCard) options.card);
                            graphics.removeHand();
                            break;
                        default:return;
                    }
                    break;
                case ACTIONSPECIFICATION:
                    switch (interaction) {
                        case CARDCLICK:
                            options.check(false, false, false, true);
                            chosenCard = options.card;
                            waitingState = State.NULL;
                            action(setInteraction, setInteractionOptions);
                            break;
                        case FIELDCLICK:
                            options.check(true, false, false, false);
                            setInteractionOptions.check(false, true, false, false);
                            options.character = setInteractionOptions.character;
                            reset();
                            usedActions += character.move(options.field, options.character);
                            break;
                    }
                    break;
                case SHARECARD:
                    switch (interaction){
                        case CHARACTERCLICK:
                            options.check(false, true, false, false);
                            break;
                        case CARDCLICK:
                            options.check(false, false, false, true);
                            break;
                        case OTHERCARDCLICK:
                            options.check(false, false, true, true);
                            usedActions += options.player.character.giveCard(this.character, (CityCard) options.card);
                            break;
                        default: return;
                    }
                    if(usedActions == 0){
                        setInteractionOptions.add(options);
                        try{
                            setInteractionOptions.check(false, true, false, true);
                            usedActions += this.character.giveCard(setInteractionOptions.character, (CityCard) setInteractionOptions.card);
                        }catch (InvalidParameter e){
                           graphics.message("Choose a card and a character!");
                        }
                    }
                    break;
                case EVENTSPECIFICATION:
                case FINISHROUNDWITHEVENT:
                    switch (interaction){
                        case CHARACTERCLICK:
                            options.check(false, true, false, false);
                            setEventOptions.add(new EventCard.EventOptions.Builder().setCharacter(options.character).build());
                            playEvent((EventCard) chosenCard);
                            break;
                        case FIELDCLICK:
                            options.check(true, false, false, false);
                            setEventOptions.add(new EventCard.EventOptions.Builder().setField(options.field).build());
                            playEvent((EventCard) chosenCard);
                            break;
                        case INFECTIONTRASHCARDCLICK:
                            options.check(false, false, false, true);
                            manageResilentPopulation(options.card);
                            break;
                        case INFECTIONDECKCARDCLICK:
                            options.check(false, false, false, true);
                            manageForecast((CityCard) options.card);
                            break;
                        default: return;
                    }
                    break;
                case FINISHROUND:
                    if(interaction == Interaction.CARDCLICK){
                        options.check(false, false, false, true);
                        Card chosenCard = this.hasCard(options.card.getName());
                        if(chosenCard == null) throw new InvalidParameter("You don't have a card called" + options.card.getName());
                        try{
                            EventCard event = (EventCard)chosenCard;
                            waitingState = State.FINISHROUNDWITHEVENT;
                            setEventOptions = new EventCard.EventOptions.Builder().setPlayer(this).build();
                            character.playEvent(event);
                        }
                        catch(Exception e){
                            drop(chosenCard);
                            finish();
                        }
                    }
                    break;
            }
        }
        catch(InvalidCommand internal){
            alert(internal, "Error");
        }
        catch(AmbigousAction e){
            alert(e, "Error");
            setInteraction = interaction;
            setInteractionOptions = options;
            waitingState = State.ACTIONSPECIFICATION;
        }
        catch(CannotPerformAction e){
            alert(e, "Error");
        }
        catch (Exception e){
            alert(new CannotPerformAction(""), "The requested operation ended with an error");
        }

        try { this.useAction(usedActions); }
        catch (EndRound e){  try { this.endRound(); } catch(Exception x){} }

        graphics.refresh();
    }

    private void reset(){
        waitingState = waitingState == State.FINISHROUNDWITHEVENT ? State.FINISHROUND : State.NULL;
        chosenCard = null;
        setInteractionOptions = new InteractionOptions.Builder().build();
        setInteraction = Interaction.OUTCLICK;
        setEventOptions = new EventCard.EventOptions.Builder().build();
        graphics.message("You are the " + character.getName() + ". Choose an action");
    }

    @Override
    public void draw(Card c) {
        super.draw(c);
    }

    /**
     * Plays the given event card
     * @param event
     */
    @Override
    public void playEvent(EventCard event){
        try {
            eventReady = true;
            this.chosenCard = event;
            event.play(setEventOptions);
            drop(chosenCard);
            if(eventReady) reset();
            if(waitingState == State.FINISHROUND) finish();
        } catch (CannotPerformAction e) {
            eventReady = false;
            graphics.message(e.getMessage());
        }
    }

    private void manageResilentPopulation(Card chosen){
        InfectionTrash trash = game.getTrash();
        trash.removeCard(chosen);
        graphics.removeHand();
        reset();
    }

    private void alert(PandemicException e, String title){
        graphics.alert(e, title);
        this.waitingState = State.NULL;
    }



    private enum State{
        NULL,
        ACTIONSPECIFICATION,
        EVENTSPECIFICATION,
        SHARECARD,
        FINISHROUND,
        FINISHROUNDWITHEVENT
    }
    public enum Interaction{
        CHARACTERCLICK,
        FIELDCLICK,
        CARDCLICK,
        OTHERCARDCLICK,
        TREATCLCIK,
        BUILDCLICK,
        ANTIDOTECLICK,
        SHARECLICK,
        PASSCLICK,
        RESTARTCLICK,
        INFECTIONTRASHCARDCLICK,
        INFECTIONTRASHBUTTONCLICK,
        INFECTIONDECKCARDCLICK,
        MAINTRASHBUTTONCLICK,
        MAINTRASHCARDCLICK,
        OUTCLICK
    }

    public static class InteractionOptions{
        Field field;
        Character character;
        Player player;
        Card card;

        private InteractionOptions(Field f, Character c, Player p, Card ca){
            field =f; character = c; player = p; card = ca;
        }

        void check(boolean field, boolean character, boolean player, boolean card) throws InvalidParameter {
            InvalidParameter fail = new InvalidParameter("Internal error occured");
            if(field && this.field == null ) throw fail;
            if(character && this.character == null ) throw fail;
            if(player && this.player == null ) throw fail;
            if(card && this.card == null ) throw fail;
        }

        void add(InteractionOptions o){
            if(o.field != null) this.field = o.field;
            if(o.character != null) this.character = o.character;
            if(o.player != null) this.player = o.player;
            if(o.card != null) this.card = o.card;
        }

        public static class Builder{
            Field field = null;
            Character character = null;
            Player player = null;
            Card card = null;

            public Builder setField(Field field) {
                this.field = field;
                return this;
            }

            public Builder setPlayer(Player p) {
                this.player = p;
                return this;
            }

            public Builder setCharacter(Character character) {
                this.character = character;
                return this;
            }

            public Builder setCard(Card c){
                this.card = c;
                return this;
            }

            public InteractionOptions build(){
                return new InteractionOptions(this.field, this.character, this.player, this.card);
            }
        }
    }
}
