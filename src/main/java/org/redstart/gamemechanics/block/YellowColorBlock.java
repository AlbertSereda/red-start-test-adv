package org.redstart.gamemechanics.block;

import org.redstart.annotation.ColorClass;
import org.redstart.gamemechanics.GameRoom;
import org.redstart.jsonclasses.Monster;
import org.redstart.jsonclasses.Player;

import java.util.logging.Logger;

@ColorClass(numberColor = 0)
public class YellowColorBlock implements ColorBlock {
    private static final Logger log = Logger.getLogger(YellowColorBlock.class.getName());
    @Override
    public void executeAction(GameRoom gameRoom) {
        Player player = gameRoom.getPlayer();
        Monster monster = gameRoom.getMonster();

        int countChoose = player.getBlastedBlocks().size();
        player.setShield(player.getShield() + countChoose);

        gameRoom.getGameLogic().decrementMonsterHP(monster, countChoose);
        log.info("Yellow color choose - " + gameRoom.getPlayer().getBlastedBlocks().size());
    }
}
