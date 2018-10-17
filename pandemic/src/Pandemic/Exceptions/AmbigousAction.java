package Pandemic.Exceptions;

public class AmbigousAction extends CannotPerformAction {
    public AmbigousAction(String message){
        super(message);
    }

    @Override
    protected String getType(){ return "AmbigousAction"; }

    @Override
    public String getHelp(){
        return "You get this error when you try to perform an action, you could execute multiple ways";
    }
}
