package Pandemic.View.Components;

import Pandemic.Players.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerComponent extends HandComponent {
    private Player player;
    private static List<PlayerComponent> components;

    static{
        components = new ArrayList<>();
    }

    public PlayerComponent(Player p) {
        super(p.getCards());
        player = p;
        this.setMaxWidth(0);
        PlayerComponent.components.add(this);
    }

    public static void disable(boolean b){
        for(PlayerComponent pc: components)
            pc.setMouseTransparent(b);
    }

    public void refresh(){
        this.setCards(player.getCards());
        super.refresh();
    }

    @Override
    public void show() {
        super.show();
        this.setMouseTransparent(false);
    }

    public Player getPlayer(){
        return player;
    }
}
