package Pandemic.Characters;

import Pandemic.Cards.Card;
import Pandemic.Exceptions.AmbigousAction;
import Pandemic.Exceptions.CannotPerformAction;
import Pandemic.Core.Hand;
import Pandemic.Table.Field;

public class OperationExpert extends Character {
    public OperationExpert(Hand h, Field f) {
        super(CharacterType.OperationExpert, h, f);
    }

    /**
     * An Operation expert can build a research station by discard any card in their hands
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction
     */
    @Override
    public int build() throws CannotPerformAction {
        if(hand.getCards().size() > 1) throw new AmbigousAction("Choose the card you'd like to discard");
        this.field.build();
        return 1;
    }

    /**
     * An Operation Expert moves similar as other characters, but can fly anywhere from a research station by discarding a card
     * @param f the field where the character shall move
     * @returns how many actions did it take to perform this
     * @throws CannotPerformAction
     */
    @Override
    public int move(Field f) throws CannotPerformAction {

        if(field.hasNeighbour(f)){
            this.replace(f);
            return 1;
        }

        if(this.field.hasStation() && f.hasStation()){
            this.replace(f);
            return 1;
        }

        if(field.hasStation()){
            if(hand.getCards().size() > 1) throw new AmbigousAction("Choose the card you'd like to discard");
            hand.drop(hand.getCards().get(0));
            replace(f);
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
}
