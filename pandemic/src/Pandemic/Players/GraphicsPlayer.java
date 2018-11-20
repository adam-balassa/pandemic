package Pandemic.Players;

import Pandemic.Cards.Card;
import Pandemic.Cards.CityCard;
import Pandemic.Cards.EventCard;
import Pandemic.Cards.InfectionTrash;
import Pandemic.Characters.Character;
import Pandemic.Core.Events;
import Pandemic.Core.IGame;
import Pandemic.Core.Pandemic;
import Pandemic.Exceptions.*;
import Pandemic.Table.Field;
import Pandemic.View.Scenes.GameScene;
import javafx.scene.layout.StackPane;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class GraphicsPlayer extends Player implements Serializable {

    transient private GameScene graphics;
    transient private State waitingState;
    transient private EventCard.EventOptions setEventOptions;
    transient private EventCard pendingEvent;
    transient private InteractionOptions setInteractionOptions;
    private boolean onRound = false;

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
        graphics.newRound(this);
        graphics.message("New round started for " + character.getName());
        onRound = true;
    }

    @Override
    protected void hasToDrop() {

    }

    @Override
    public void showInfection(CityCard card) {

    }

    @Override
    public List<CityCard> forecast() {
        return null;
    }

    @Override
    public InfectionTrash getTrash() {
        graphics.openInfectionTrash(game.getTrash().getCards());
        return null;
    }

    @Override
    public void replaceCards(List<CityCard> c) {

    }

    @Override
    public void endRound() throws EndOfGame {
        onRound = false;
        try{
            super.endRound();
            game.nextRound();
        }
        catch (PandemicException e){
            graphics.endGame();
        }
    }

    public void action(Interaction interaction, InteractionOptions options){
        System.out.println(waitingState == State.EVENTSPECIFICATION ? "event" : waitingState == State.SHARECARD ? "share" : "NULL");
        int usedActions = 0;
        if(interaction == Interaction.RESTARTCLICK) game.undo();
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
                        default:return;
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
                    switch (interaction){
                        case CHARACTERCLICK:
                            options.check(false, true, false, false);
                            setEventOptions.add(new EventCard.EventOptions.Builder().setCharacter(options.character).build());
                            break;
                        case FIELDCLICK:
                            options.check(true, false, false, false);
                            setEventOptions.add(new EventCard.EventOptions.Builder().setField(options.field).build());
                            break;
                        case INFECTIONTRASHCARDCLICK:
                            options.check(false, false, false, true);
                            manageResilentPopulation(options.card);
                            break;
                        case OUTCLICK:
                            reset();
                            break;
                        default: return;
                    }
                    break;
            }
        }
        catch(InvalidCommand internal){
            alert(internal, "Error");
        }
        catch(CannotPerformAction e){
            alert(e, "Error");
        }
        catch (Exception e){
            alert(new CannotPerformAction(""), "The requested operation ended with an error");
            e.printStackTrace();
        }

        try { this.useAction(usedActions); }
        catch (EndRound e){  try { this.endRound(); } catch(Exception x){} }

        graphics.refresh();
    }

    private void reset(){
        waitingState = State.NULL;
        pendingEvent = null;
        setInteractionOptions = new InteractionOptions.Builder().build();
        setEventOptions = new EventCard.EventOptions.Builder().build();
    }

    @Override
    public void draw(Card c) {
        super.draw(c);
    }

    @Override
    public void playEvent(EventCard event){
        try {
            this.waitingState = State.EVENTSPECIFICATION;
            this.pendingEvent = event;
            event.play(setEventOptions);
            drop(pendingEvent);
            pendingEvent = null;
        } catch (CannotPerformAction e) {
            graphics.message(e.getMessage());
        }
    }

    private void manageResilentPopulation(Card chosen){
        InfectionTrash trash = game.getTrash();
        trash.removeCard(chosen);
        reset();
    }
    
    public void alert(PandemicException e, String title){
        graphics.alert(e, title);
        this.waitingState = State.NULL;
    }

    private enum State{
        NULL,
        EVENTSPECIFICATION,
        SHARECARD
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
