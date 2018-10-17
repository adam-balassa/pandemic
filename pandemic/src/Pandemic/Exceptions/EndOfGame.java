package Pandemic.Exceptions;

public class EndOfGame extends PandemicException {
    public EndOfGame(String msg){
        super(msg);
    }

    @Override
    protected String getType() {
        return "EndOfGame";
    }

    @Override
    public String getHelp() {
        return "You lost the game.";
    }
}
