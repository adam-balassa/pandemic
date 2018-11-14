package Pandemic.Players;

import Pandemic.Cards.CityCard;
import Pandemic.Core.IGame;
import Pandemic.Exceptions.EndOfGame;
import Pandemic.View.Scenes.GameScene;

import java.io.Serializable;
import java.util.List;

public class GraphicsPlayer extends Player implements Serializable {

    transient private GameScene graphics;

    public GraphicsPlayer(IGame g, GameScene graphicsContext) {
        super(g);
        graphics = graphicsContext;
    }

    @Override
    public void round() throws EndOfGame {
        super.round();
        graphics.newRound(this);
        graphics.message("New round started for " + getName());
    }

    @Override
    protected void hasToDrop() {

    }

    @Override
    public void showInfection(CityCard card) {

    }

    @Override
    public List<CityCard> forecast() {
        return null;
    }

    @Override
    public List<CityCard> getTrash() {
        return null;
    }

    @Override
    public void replaceCards(List<CityCard> c) {

    }

    public void test(){
        System.out.println("Test successful");
    }
}
