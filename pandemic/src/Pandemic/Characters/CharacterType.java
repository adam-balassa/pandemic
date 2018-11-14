package Pandemic.Characters;

import Pandemic.Core.Hand;
import Pandemic.Table.Field;
import javafx.scene.paint.Color;

import java.io.Serializable;

public enum CharacterType implements Serializable {
    Medic("Medic", Color.color(0.9, 0.3, 0.1)){
        @Override
        public Character createCharacter(Hand h, Field f) {
            return new Medic(h, f);
        }
    },
    ContingencyPlanner("Contingency Planner", Color.color(0, 0.7, 0.9)){
        public Character createCharacter(Hand h, Field f) {
            return new ContingencyPlanner(h, f);
        }
    },
    Researcher("Researcher", Color.color(0.4, 0.2, 0)){
        public Character createCharacter(Hand h, Field f) {
            return new Researcher(h, f);
        }
    },
    Scientist("Scientist", Color.color(0.9, 0.9, 0.9)){
        public Character createCharacter(Hand h, Field f) {
            return new Scientist(h, f);
        }
    },
    QuarantineSpecialist("Quarantine Specialist", Color.color(0.1, 0.4, 0)){
        public Character createCharacter(Hand h, Field f) {
            return new QuarantineSpecialist(h, f);
        }
    },
    Dispatcher("Dispatcher", Color.color(0.56, 0, 0.5)){
        public Character createCharacter(Hand h, Field f) {
            return new Dispatcher(h, f);
        }
    },
    OperationExpert("Operation Expert", Color.color(0.1, 0.7, 0.1)){
        public Character createCharacter(Hand h, Field f) {
            return new OperationExpert(h, f);
        }
    };

    public final String name;
    public final Color color;
    CharacterType(String n, Color c){
        name = n;
        color = c;
    }

    abstract public Character createCharacter(Hand h, Field f);
}
