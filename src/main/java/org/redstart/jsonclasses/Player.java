package org.redstart.jsonclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.redstart.gamemechanics.spells.interfaces.Spell;
import org.redstart.gamemechanics.spells.interfaces.WithTimeSpell;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Player {
    private String name;
    private int hp;
    private int mana;
    private int shield;
    @JsonIgnore
    private int[][][] fieldForServer; //имя, индекс, цвет
    private List<Integer> blastedBlocks; //имена уничтоженных
    private List<int[]> spawnedBlocks; //массив заспавненых имя, индекс, цвет.
    @JsonIgnore
    private final List<Spell> availableSpells;
    @JsonIgnore
    private final List<WithTimeSpell> activeSpell;

    @JsonIgnore
    private int nextNameBlock = 1;

    public Player(String name, int hp, int mana, int shield) {
        this.name = name;
        this.hp = hp;
        this.mana = mana;
        this.shield = shield;
        blastedBlocks = new ArrayList<>();
        spawnedBlocks = new ArrayList<>();
        availableSpells = new ArrayList<>();
        activeSpell = new CopyOnWriteArrayList<>();
    }

    public void addAvailableSpell(Spell spell) {
        availableSpells.add(spell);
    }

    public void addActiveSpell(WithTimeSpell withTimeSpell) {
        activeSpell.add(withTimeSpell);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int[][][] getFieldForServer() {
        return fieldForServer;
    }

    public void setFieldForServer(int[][][] fieldForServer) {
        this.fieldForServer = fieldForServer;
    }

    public List<Integer> getBlastedBlocks() {
        return blastedBlocks;
    }

    public void setBlastedBlocks(List<Integer> blastedBlocks) {
        this.blastedBlocks = blastedBlocks;
    }

    public List<int[]> getSpawnedBlocks() {
        return spawnedBlocks;
    }

    public void setSpawnedBlocks(List<int[]> spawnedBlocks) {
        this.spawnedBlocks = spawnedBlocks;
    }

    public int getNextNameBlock() {
        return nextNameBlock++;
    }

    public void setNextNameBlock(int nextNameBlock) {
        this.nextNameBlock = nextNameBlock;
    }

    public int getMana() {
        return mana;
    }

    public void setMana(int mana) {
        this.mana = mana;
    }

    public int getShield() {
        return shield;
    }

    public void setShield(int shield) {
        this.shield = shield;
    }

    public List<Spell> getAvailableSpells() {
        return availableSpells;
    }

    public List<WithTimeSpell> getActiveSpell() {
        return activeSpell;
    }
}
