package Pandemic.View.Components;

import Pandemic.Players.GraphicsPlayer;
import Pandemic.Players.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerComponent extends HandComponent {
    private Player player;
    private GraphicsPlayer currentPlayer;
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

    public void refresh(GraphicsPlayer newCurrentPlayer){
        this.setCards(player.getCards());
        this.currentPlayer = newCurrentPlayer;
        super.refresh();
        for(CardComponent card : getCards())
            card.setOnMouseClicked(e -> {
                if(player == currentPlayer)
                    currentPlayer.action(
                        GraphicsPlayer.Interaction.CARDCLICK,
                        new GraphicsPlayer.InteractionOptions.Builder().setCard(card.getCard()).build()
                    );
                else
                    currentPlayer.action(
                        GraphicsPlayer.Interaction.OTHERCARDCLICK,
                        new GraphicsPlayer.InteractionOptions.Builder().setCard(card.getCard()).build()
                    );
            });
    }

    @Override
    public void refresh() {
        this.refresh(currentPlayer);
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
