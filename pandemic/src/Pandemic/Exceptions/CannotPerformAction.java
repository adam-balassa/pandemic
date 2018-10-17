package Pandemic.Exceptions;


public class CannotPerformAction extends PandemicException {
    public CannotPerformAction(String message){
        super(message);
    }

    @Override
    protected String getType(){ return "CannotPerformAction"; }

    @Override
    public String getHelp(){
        return "You get this error when you try to perform an action, " +
                "that's execution is not possible considering your abilities and resources.";
    }
}
