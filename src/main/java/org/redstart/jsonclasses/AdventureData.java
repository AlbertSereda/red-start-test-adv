package org.redstart.jsonclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class AdventureData {
    private Player player;
    private Monster monster;

    @JsonIgnore
    private List<int[]> spawnedBlocks;

    public AdventureData(Player player, Monster monster) {
        this.player = player;
        this.monster = monster;
        spawnedBlocks = player.getSpawnedBlocks();
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

    public List<int[]> getSpawnedBlocks() {
        return spawnedBlocks;
    }

    public void setSpawnedBlocks(List<int[]> spawnedBlocks) {
        this.spawnedBlocks = spawnedBlocks;
    }
}
