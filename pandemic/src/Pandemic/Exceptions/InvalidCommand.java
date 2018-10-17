package Pandemic.Exceptions;

public class InvalidCommand extends PandemicException {
    public InvalidCommand(String message){
        super(message);
    }

    @Override
    protected String getType(){ return "InvalidCommand"; }

    @Override
    public String getHelp(){
        return "You get this error when you try to execute an invalid command";
    }
}
