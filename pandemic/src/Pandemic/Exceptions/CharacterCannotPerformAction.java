package Pandemic.Exceptions;

public class CharacterCannotPerformAction extends CannotPerformAction {
    public CharacterCannotPerformAction(String message){
        super(message);
    }

    @Override
    protected String getType(){ return "CharacterCannotPerformAction"; }

    @Override
    public String getHelp(){
        return "You get this error when you try to perform an action, that your character isn't capable of performing";
    }
}
