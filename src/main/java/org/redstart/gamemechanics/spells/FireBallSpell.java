package org.redstart.gamemechanics.spells;

import org.redstart.gamemechanics.GameRoom;
import org.redstart.gamemechanics.spells.interfaces.OneTimeSpell;

import java.util.logging.Logger;

public class FireBallSpell implements OneTimeSpell {
    private static final Logger log = Logger.getLogger(FireBallSpell.class.getName());
    private final GameRoom gameRoom;
    private final int cost;
    private final int damage;

    public FireBallSpell(GameRoom gameRoom, int cost, int damage) {
        this.gameRoom = gameRoom;
        this.cost = cost;
        this.damage = damage;
    }

    public void activate() {
        log.info("FireBallSpell activate");
        decrementMana(gameRoom.getPlayer(), cost);
        gameRoom.getGameLogic().decrementMonsterHP(gameRoom.getMonster(), damage);
    }

    public int getCost() {
        return cost;
    }

    public int getDamage() {
        return damage;
    }
}
