package org.redstart.jsonclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.redstart.gamemechanics.logicstrategy.interfaces.MonsterMoveLogic;
import org.redstart.gamemechanics.logicstrategy.interfaces.UpdateSpeedLogic;

public class Monster {
    private String name;
    private int hp;
    private int maxSpeed;
    private int currentSpeed;

    @JsonIgnore
    private Long timeCreation;
    @JsonIgnore
    private MonsterMoveLogic monsterMoveLogic;
    @JsonIgnore
    private UpdateSpeedLogic updateSpeedLogic;

    public Monster(String name, int hp, int maxSpeed, MonsterMoveLogic monsterMoveLogic, UpdateSpeedLogic updateSpeedLogic) {
        this.name = name;
        this.hp = hp;
        this.maxSpeed = maxSpeed;
        this.monsterMoveLogic = monsterMoveLogic;
        this.updateSpeedLogic = updateSpeedLogic;
    }

    public void setNewTimeCreation() {
        timeCreation = System.currentTimeMillis();
        currentSpeed = maxSpeed;
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

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    @Override
    public String toString() {
        return "Monster{" +
                "name='" + name + '\'' +
                ", hp=" + hp +
                ", maxSpeed=" + maxSpeed +
                ", currentSpeed=" + currentSpeed +
                '}';
    }

    public Long getTimeCreation() {
        return timeCreation;
    }

    public void setTimeCreation(Long timeCreation) {
        this.timeCreation = timeCreation;
    }

    public MonsterMoveLogic getMonsterMoveLogic() {
        return monsterMoveLogic;
    }

    public void setMonsterMoveLogic(MonsterMoveLogic monsterMoveLogic) {
        this.monsterMoveLogic = monsterMoveLogic;
    }

    public UpdateSpeedLogic getUpdateSpeedLogic() {
        return updateSpeedLogic;
    }

    public void setUpdateSpeedLogic(UpdateSpeedLogic updateSpeedLogic) {
        this.updateSpeedLogic = updateSpeedLogic;
    }
}
