package Pandemic.Characters;

import Pandemic.Exceptions.UnnecessaryAction;
import Pandemic.Core.Hand;
import Pandemic.Table.Field;

public class QuarantineSpecialist extends Character {
    public QuarantineSpecialist(Hand h, Field f) {
        super(CharacterType.QuarantineSpecialist, h, f);
        setQuarantine();
    }

    @Override
    public void replace(Field f) throws UnnecessaryAction {
        this.removeQuarantine();
        super.replace(f);
        this.setQuarantine();
    }

    private void removeQuarantine(){
        this.field.removeQuarantine();
        for (Field field: this.field.getNeighbours()) {
            field.removeQuarantine();
        }
    }

    private void setQuarantine(){
        this.field.setQuarantine();
        for (Field field: this.field.getNeighbours()) {
            field.setQuarantine();
        }
    }
}
