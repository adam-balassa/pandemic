package Pandemic.Exceptions;

public class UnnecessaryAction extends CannotPerformAction {
    public UnnecessaryAction(String message){
        super(message);
    }

    @Override
    protected String getType(){ return "UnnecessaryAction"; }

    @Override
    public String getHelp(){
        return "You get this error when you try to perform an action, that would cost you an action, but will make no changes";
    }
}
