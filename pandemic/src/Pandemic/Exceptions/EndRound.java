package Pandemic.Exceptions;

public class EndRound extends PandemicException {
    public EndRound() {
        super("Your round is ended");
    }

    @Override
    protected String getType() {
        return "EndRound";
    }

    @Override
    public String getHelp() {
        return "Your round is ended";
    }
}
