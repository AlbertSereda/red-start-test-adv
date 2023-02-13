package org.redstart.gamemechanics.spells;

import org.redstart.gamemechanics.GameRoom;
import org.redstart.gamemechanics.logicstrategy.interfaces.UpdateSpeedLogic;
import org.redstart.gamemechanics.spells.interfaces.WithTimeSpell;
import org.redstart.jsonclasses.Monster;

import java.util.logging.Logger;

public class StanSpell implements WithTimeSpell, UpdateSpeedLogic {
    private static final Logger log = Logger.getLogger(StanSpell.class.getName());
    private final GameRoom gameRoom;
    private final int cost;
    private final long actionTime;
    private final int damage;

    private long timeCreation;
    private long timeToEnd;
    private UpdateSpeedLogic oldUpdateSpeedLogic;

    private long timeMoveMonsterPassed;

    private boolean isActive;

    public StanSpell(GameRoom gameRoom, int cost, long actionTime, int damage) {
        this.gameRoom = gameRoom;
        this.cost = cost;
        this.actionTime = actionTime;
        timeToEnd = actionTime;
        this.damage = damage;
        isActive = false;
    }

    @Override
    public void activate() {
        decrementMana(gameRoom.getPlayer(), cost);
        resetSpell();
        timeCreation = System.currentTimeMillis();

        if (!isActive) {
            Monster monster = gameRoom.getMonster();
            oldUpdateSpeedLogic = monster.getUpdateSpeedLogic();

            timeMoveMonsterPassed = System.currentTimeMillis() - monster.getTimeCreation();
            monster.setUpdateSpeedLogic(this);

            gameRoom.getPlayer().addActiveSpell(this);
        }
        log.info("StanSpell activate");
    }

    @Override
    public void deactivate() {
        Monster monster = gameRoom.getMonster();
        monster.setUpdateSpeedLogic(oldUpdateSpeedLogic);
        isActive = false;
        log.info("StanSpell deactivate");
    }

    @Override
    public void update() {
        long timePassed = System.currentTimeMillis() - timeCreation;
        timeToEnd = actionTime - timePassed;
    }

    @Override
    public int updateCurrentSpeed(long maxSpeed, long timeCreation) {
        long updateTimeCreation = System.currentTimeMillis() - timeMoveMonsterPassed;

        Monster monster = gameRoom.getMonster();
        monster.setTimeCreation(updateTimeCreation);

        return monster.getCurrentSpeed();
    }

    @Override
    public int getCost() {
        return cost;
    }


    @Override
    public long getTimeToEnd() {
        return timeToEnd;
    }


    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public void resetSpell() {
        timeToEnd = actionTime;
    }
}
