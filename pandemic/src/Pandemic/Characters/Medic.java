package Pandemic.Characters;

import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Hand;
import Pandemic.Table.Field;
import Pandemic.Virus;

public class Medic extends Character {
    public Medic(Hand h, Field f) {
        super(CharacterType.Medic, h, f);
    }

    @Override
    public int move(Field f) throws CannotPerformAction {
        int a = super.move(f);
        this.cleanAll();
        return a;
    }

    @Override
    public int clean(Virus v) throws CannotPerformAction {
        field.clearAll(v);
        return 1;
    }

    private void cleanAll() {
        try {
            if(hand.isAntidoteMade(Virus.RED))
                this.clean(Virus.RED);
            if(hand.isAntidoteMade(Virus.BLACK))
                this.clean(Virus.BLACK);
            if(hand.isAntidoteMade(Virus.BLUE))
                this.clean(Virus.BLUE);
            if(hand.isAntidoteMade(Virus.YELLOW))
                this.clean(Virus.YELLOW);
        } catch (CannotPerformAction cannotPerformAction) { }
    }
}
