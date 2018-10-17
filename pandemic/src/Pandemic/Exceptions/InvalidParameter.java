package Pandemic.Exceptions;

public class InvalidParameter extends InvalidCommand {
    public InvalidParameter(String message){
        super(message);
    }

    @Override
    protected String getType(){ return "InvalidParameter"; }

    @Override
    public String getHelp(){
        return "You get this error when you try to execute a valid command with invalid parameters";
    }
}
