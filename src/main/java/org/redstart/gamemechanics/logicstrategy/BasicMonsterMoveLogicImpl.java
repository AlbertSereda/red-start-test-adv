package org.redstart.gamemechanics.logicstrategy;

import org.redstart.gamemechanics.GameLogic;
import org.redstart.gamemechanics.GameRoom;
import org.redstart.gamemechanics.logicstrategy.interfaces.MonsterMoveLogic;

public class BasicMonsterMoveLogicImpl implements MonsterMoveLogic {

    private final GameRoom gameRoom;

    public BasicMonsterMoveLogicImpl(GameRoom gameRoom) {
        this.gameRoom = gameRoom;
    }

    public void monsterMove() {
        GameLogic gameLogic = gameRoom.getGameLogic();
        int damage = (int) (Math.random() * 10) + 10;
        gameLogic.decrementPlayerHp(gameRoom.getPlayer(), damage);
    }
}