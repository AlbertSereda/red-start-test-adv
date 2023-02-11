package org.redstart.gamemechanics.spells;

import org.redstart.gamemechanics.GameRoom;
import org.redstart.gamemechanics.logicstrategy.interfaces.MonsterMoveLogic;
import org.redstart.gamemechanics.spells.interfaces.DelayedSpell;
import org.redstart.jsonclasses.Monster;

import java.util.logging.Logger;

public class NextDamageProtectionSpell implements DelayedSpell, MonsterMoveLogic {
    private static final Logger log = Logger.getLogger(NextDamageProtectionSpell.class.getName());
    private final GameRoom gameRoom;
    private final int cost;
    private final int damage;
    private MonsterMoveLogic oldMonsterMoveLogic;
    private boolean isActive = false;

    public NextDamageProtectionSpell(GameRoom gameRoom, int cost, int damage) {
        this.gameRoom = gameRoom;
        this.cost = cost;
        this.damage = damage;
    }

    public void activate() {
        log.info("NextDamageProtectionSpell activate");
        if (!isActive) {
            decrementMana(gameRoom.getPlayer(), cost);

            Monster monster = gameRoom.getMonster();
            oldMonsterMoveLogic = monster.getMonsterMoveLogic();
            monster.setMonsterMoveLogic(this);
            isActive = true;
        }
    }

    public int getCost() {
        return cost;
    }

    public int getDamage() {
        return damage;
    }

    public void monsterMove() {
        Monster monster = gameRoom.getMonster();
        monster.setMonsterMoveLogic(oldMonsterMoveLogic);
        isActive = false;
    }

}
