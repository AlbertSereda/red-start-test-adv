package org.redstart.jsonclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class AdventureData {
    private Player player;
    private Monster monster;

    public AdventureData(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
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

}
