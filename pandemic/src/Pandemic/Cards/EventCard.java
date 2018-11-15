package Pandemic.Cards;

import Pandemic.Core.Events;
import Pandemic.Core.Virus;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Exceptions.UnnecessaryAction;
import Pandemic.Players.Player;
import Pandemic.Table.Field;
import Pandemic.Characters.Character;
import Pandemic.View.Components.CardComponent;
import Pandemic.View.Components.CityCardComponent;
import Pandemic.View.Components.EventCardComponent;
import javafx.geometry.Point2D;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EventCard extends Card implements Serializable {
    private EventTypes type;
    public EventCard(EventTypes t) {
        super(t.getName());
        type = t;
    }

    @Override
    public void draw(Player p) {
        p.draw(this);
    }

    public String getDescription(){
        return type.getDescription();
    }

    public void play(EventOptions o) throws CannotPerformAction{
        this.type.play(o);
    }

    @Override
    boolean isEvent(){ return true; }

    @Override
    public CardComponent getDrawer() {
        return new EventCardComponent(this);
    }

    public enum EventTypes{
        FORECAST("Forecast"){
            @Override
            String getDescription() {
                return "Check the first 6 cards in the infection-deck, and sort in a custom order";
            }

            @Override
            void play(EventOptions o) throws CannotPerformAction {
                if(o.player == null) throw new CannotPerformAction("Choose a character to play this event card");
                o.player.replaceCards(o.player.forecast());
            }
        },
        SILENTNIGHT("Silent Night"){
            @Override
            String getDescription() {
                return "Skip the infection phase this round";
            }

            @Override
            void play(EventOptions o) throws CannotPerformAction {
                if(o.player == null) throw new CannotPerformAction("Choose a character to play this event card");
                o.player.dontInfect();
            }
        },
        RESILENTPPULATION("Resilent Population"){
            @Override
            String getDescription() {
                return "Remove a custom city card from the infection-trash.";
            }

            @Override
            void play(EventOptions o) throws CannotPerformAction {
                if(o.player == null) throw new CannotPerformAction("Choose a character to play this event card");
                o.player.getTrash();
            }
        },
        AIRLIFT("Airlift"){
            @Override
            String getDescription() {
                return "Move any player to any city";
            }

            @Override
            void play(EventOptions o) throws CannotPerformAction {
                if(o.character == null) throw new CannotPerformAction("Choose a character to play this event card");
                if(o.field == null) throw new CannotPerformAction("Choose a field to play this event card");
                try{
                    o.character.replace(o.field);
                }catch (UnnecessaryAction e){}
            }
        },
        GOVERNMENTGRANT("Governmant Grant"){
            @Override
            String getDescription() {
                return "Place a research station anywhere you like";
            }

            @Override
            void play(EventOptions o) throws CannotPerformAction {
                if(o.field == null) throw new CannotPerformAction("Choose a field to play this event card");
                try {
                    o.field.build();
                } catch (UnnecessaryAction unnecessaryAction) {}
            }
        };

        private final String name;
        EventTypes(String name){
            this.name = name;
        }

        String getName() {
            return name;
        }

        abstract String getDescription();
        abstract void play(EventOptions o) throws CannotPerformAction;

        public static List<EventCard> getAllEventCards(){
            List<EventCard> cards = new ArrayList<>(5);
            for (EventTypes e: EventTypes.values())
                cards.add(new EventCard(e));
            return cards;
        }
    }

    public static class EventOptions{
        Events player;
        Field field;
        Character character;
        private EventOptions(Events e, Field f, Character c){
            player = e;
            field = f;
            character = c;
        }

        public static class Builder{
            private Events player = null;
            private Field field = null;
            private Character character = null;

            public Builder setField(Field field) {
                this.field = field;
                return this;
            }

            public Builder setPlayer(Events game) {
                this.player = game;
                return this;
            }

            public Builder setCharacter(Character character) {
                this.character = character;
                return this;
            }

            public EventOptions build(){
                return new EventOptions(this.player, this.field, this.character);
            }
        }
    }
}
