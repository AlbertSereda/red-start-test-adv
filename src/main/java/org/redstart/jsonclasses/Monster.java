package org.redstart.jsonclasses;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Monster {
    private String name;
    private int hp;
    private int maxSpeed;
    private int currentSpeed;

    @JsonIgnore
    private Long timeCreation;

    public Monster(String name, int hp, int maxSpeed) {
        this.name = name;
        this.hp = hp;
        this.maxSpeed = maxSpeed;
    }

    public void setNewTimeCreation() {
        timeCreation = System.currentTimeMillis();
        currentSpeed = maxSpeed;
    }

    public void updateCurrentSpeed() {
        long timePassed = System.currentTimeMillis() - timeCreation;
        currentSpeed = (int) (maxSpeed - timePassed);

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
}
