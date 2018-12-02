package Pandemic.Characters;

import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Core.Hand;
import Pandemic.Table.Field;
import Pandemic.Core.Virus;

public class Medic extends Character {
    public Medic(Hand h, Field f) {
        super(CharacterType.Medic, h, f);
    }

    /**
     * A Medic automatically cleans the field where they moved if the antidote of the given color is already made
     * @param f the field where the character shall move
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction
     */
    @Override
    public int move(Field f) throws CannotPerformAction {
        int a = super.move(f);
        this.cleanAll();
        return a;
    }

    /**
     * A Medic cleans all the virus of the given color when they clean
     * @param v the color of the virus that the user wants to remove
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction
     */
    @Override
    public int clean(Virus v) throws CannotPerformAction {
        field.clearAll(v);
        return 1;
    }

    /**
     * Checks if the antidote is made for each color and cleans
     */
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
