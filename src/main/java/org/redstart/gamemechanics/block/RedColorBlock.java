package org.redstart.gamemechanics.block;

import org.redstart.annotation.ColorClass;
import org.redstart.gamemechanics.GameRoom;
import org.redstart.jsonclasses.Monster;
import org.redstart.jsonclasses.Player;

import java.util.logging.Logger;

@ColorClass(numberColor = 1)
public class RedColorBlock implements ColorBlock {
    private static final Logger log = Logger.getLogger(RedColorBlock.class.getName());
    @Override
    public void executeAction(GameRoom gameRoom) {
        Player player = gameRoom.getPlayer();
        Monster monster = gameRoom.getMonster();

        int countChoose = player.getBlastedBlocks().size();
        if (countChoose >= 10) {
            countChoose *= 2;
        }
        int countDamage = countChoose * 2;
        gameRoom.getGameLogic().decrementMonsterHP(monster, countDamage);
        log.info("Red color choose - " + gameRoom.getPlayer().getBlastedBlocks().size());
    }
}
