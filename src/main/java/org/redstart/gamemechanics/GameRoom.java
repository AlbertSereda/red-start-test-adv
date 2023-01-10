package org.redstart.gamemechanics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.redstart.jsonclasses.AdventureData;
import org.redstart.jsonclasses.Monster;
import org.redstart.jsonclasses.Player;

public class GameRoom {
    private Player player;
    private Monster monster;

    @JsonIgnore
    private GameLogic gameLogic;

    private AdventureData adventureData;

    public GameRoom(GameLogic gameLogic) {
        player = new Player("RedStart", 100, 0, 0);
        monster = new Monster("Tanos", 100, 20);
        this.gameLogic = gameLogic;
        gameLogic.fillFieldForServer(player);
        adventureData = new AdventureData(player, monster);
    }



    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public AdventureData getAdventureData() {
        return adventureData;
    }
}
